package com.zodo.kart.repository.product;

import com.zodo.kart.dto.product.CategoryDTO;
import com.zodo.kart.entity.product.Category;
import com.zodo.kart.entity.product.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author : Bhanu prasad
 */

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

//
//    @Query("SELECT new com.zodo.kart.dto.product.CategoryDTO(c.categoryId, c.categoryName, c.categoryBanner.imageUrl, c.categoryImage.imageUrl, "
//            + "sc.subCategoryId, sc.subCategoryName, sc.subCategoryImage.imageUrl) "
//            + "FROM Category c "
//            + "LEFT JOIN c.subCategories sc "
//            + "WHERE c.categoryId = :categoryId")
//    CategoryDTO findCategoryWithSubCategories(Long categoryId);

    // Fetch the category by ID
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.subCategories WHERE c.categoryId = :categoryId")
    Category findCategoryByIdWithSubCategories(Long categoryId);

    // Fetch subcategories by Category ID
    @Query("SELECT sc FROM SubCategory sc WHERE sc.category.categoryId = :categoryId")
    List<SubCategory> findSubCategoriesByCategoryId(Long categoryId);
}
