package com.zodo.kart.controller.product;

import com.zodo.kart.dto.product.CategoryDTO;
import com.zodo.kart.entity.product.Category;
import com.zodo.kart.entity.product.Product;
import com.zodo.kart.entity.product.SubCategory;
import com.zodo.kart.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Author : Bhanu prasad
 */

@RestController
@RequestMapping("/public/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productService.saveProduct(product));
    }

    // save category
    @PostMapping("/add-category")
    @Procedure(MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addCategory(@RequestBody Category category) {
        return ResponseEntity.ok(productService.saveCategory(category));
    }


    // save sub category
    @PostMapping("/add-sub-category")
    public ResponseEntity<?> addSubCategory(@RequestBody SubCategory subcategory) {
        return ResponseEntity.ok(productService.saveSubCategory(subcategory));
    }

    // get product by id
    @GetMapping("/get-product-by-id/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }
    // get category by id
    @GetMapping("/get-category-by-id/{categoryId}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.getCategoryById(categoryId));
    }
    // get sub category by id
    @GetMapping("/get-sub-category-by-id/{subCategoryId}")
    public ResponseEntity<?> getSubCategoryById(@PathVariable Long subCategoryId) {
        return ResponseEntity.ok(productService.getSubCategoryById(subCategoryId));
    }

    @GetMapping("/find-category-with-sub-categories")
    public ResponseEntity<List<CategoryDTO>> findCategoryWithSubCategories() {
        return ResponseEntity.ok(productService.findCategoryWithSubCategories());
    }

    // find sub categories by category id
    @GetMapping("/find-sub-categories-by-category-id/{categoryId}")
    public ResponseEntity<CategoryDTO> findSubCategoriesByCategoryId(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.findSubCategoriesByCategoryId(categoryId));
    }

    @GetMapping("/all-products")
    public ResponseEntity<?> findAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // get all categories
    @GetMapping("/all-categories")
    public ResponseEntity<List<Category>> findAllCategories() {
        return ResponseEntity.ok(productService.findAllCategories());
    }

}
