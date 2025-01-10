package com.zodo.kart.service.product;

import com.zodo.kart.dto.product.CategoryDTO;
import com.zodo.kart.dto.product.SubCategoryDTO;
import com.zodo.kart.entity.product.*;
import com.zodo.kart.repository.Image.ImageRepository;
import com.zodo.kart.repository.product.CategoryRepository;
import com.zodo.kart.repository.product.PackRepository;
import com.zodo.kart.repository.product.ProductRepository;
import com.zodo.kart.repository.product.SubCategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



/**
 * Author : Bhanu prasad
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final PackRepository packRepository;
    private final ImageRepository imageRepository;


    @Transactional
    public Product saveProduct(Product product) {

        List<Pack> productPacks = new ArrayList<>();

        for(Pack pack : product.getAvailablePacks()){
            Pack p = new Pack();
            p.setPackName(pack.getPackName());
            p.setPackPrice(pack.getPackPrice());
            p.setPackSku(pack.getPackSku()); // generate pack sku


            List<Image> images = pack.getImages().stream().map(image ->{
                Image img = new Image();
                img.setImageUrl(image.getImageUrl());
                img.setPack(p);
                return img;
            }).toList();


            p.setImages(images); // set pack images (image image.setPack(p)).toList())
            p.setProduct(product);
            productPacks.add(p);
        }
        product.setAvailablePacks(productPacks);

        return productRepository.save(product);
    }


    // save category
    public CategoryDTO saveCategory(Category category) {
       Image categoryBanner = category.getCategoryBanner();
       Image categoryImage = category.getCategoryImage();
       imageRepository.save(categoryBanner);
       imageRepository.save(categoryImage);
       category.setCategoryBanner(categoryBanner);
       category.setCategoryImage(categoryImage);

       log.warn("category" + category);

       //log.warn("category1" + category1);

      List<SubCategory> subCategories = category.getSubCategories();
      category.setSubCategories(null);
      Category category1 = categoryRepository.save(category);

        if(subCategories!=null) {
            subCategories.stream().map(subc -> {
                        SubCategory subCategory = subc;
                        System.out.println(subc);

                        subc.setCategory(category1);
                        return saveSubCategory(subc);
                    }
            ).toList();


            category1.setSubCategories(subCategories);
        }
        categoryRepository.save(category1);

        return findSubCategoriesByCategoryId(category1.getCategoryId());
    }

    // save sub category
    public SubCategory saveSubCategory(SubCategory subCategory) {
        Image subCategoryImage = subCategory.getSubCategoryImage();
        subCategory.setSubCategoryImage(imageRepository.save(subCategoryImage));
        return subCategoryRepository.save(subCategory);
    }

    // get product by id
    @Transactional
    public Product getProductById(Long productId) {
        return productRepository.findById(productId).get();
    }
    // get category by id

    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).get();
    }
    // get sub category by id
    public SubCategory getSubCategoryById(Long subCategoryId) {
        return subCategoryRepository.findById(subCategoryId).get();
    }

    // get all categories
    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    // get all sub categories
    public List<SubCategory> findAllSubCategories() {
        return subCategoryRepository.findAll();
    }


    public List<CategoryDTO> findCategoryWithSubCategories() {

        List<SubCategory> subCategories = subCategoryRepository.findAll();


        Map<CategoryDTO, List<SubCategory>> categoryMap = subCategories.stream()
                .collect(Collectors.groupingBy(
                        subCategory -> CategoryDTO.builder()
                                .categoryId(subCategory.getCategory().getCategoryId())
                                .categoryName(subCategory.getCategory().getCategoryName())
                                .categoryBanner(subCategory.getCategory().getCategoryBanner())
                                .categoryImage(subCategory.getCategory().getCategoryImage())
                                .build()
                ));

        return categoryMap.entrySet().stream()
                .map(entry -> {
                    CategoryDTO categoryDTO = entry.getKey();
                    categoryDTO.setSubCategories(entry.getValue().stream().map(this::convertSubCategoryToSubCategoryDto).toList());
                    return categoryDTO;
                })
                .collect(Collectors.toList());
    }

// find sub categories by category id
    public CategoryDTO findSubCategoriesByCategoryId(Long categoryId){
        Category category = categoryRepository.findById(categoryId).get();
        return CategoryDTO.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .categoryBanner(category.getCategoryBanner())
                .categoryImage(category.getCategoryImage())
                .subCategories(subCategoryRepository.findByCategoryCategoryId(categoryId).stream().map(this::convertSubCategoryToSubCategoryDto).toList())
                .build();

    }

    // create new product --- add avilable packs (1kg, 2kg packs)  1. crate new pack 2. update pack
    // update product
    // delete product


    // get product

    // get all products



    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }




    // get products by category
    // get products by sub category


    // convert sub category to sub category dto
    public SubCategoryDTO convertSubCategoryToSubCategoryDto(SubCategory subCategory){
        return SubCategoryDTO.builder()
                .subCategoryId(subCategory.getSubCategoryId())
                .subCategoryName(subCategory.getSubCategoryName())
                .subCategoryImage(subCategory.getSubCategoryImage())
                .build();
    }


}
