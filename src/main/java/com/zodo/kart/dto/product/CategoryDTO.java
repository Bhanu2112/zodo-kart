package com.zodo.kart.dto.product;

import com.zodo.kart.entity.product.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Author : Bhanu prasad
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDTO {
    private Long categoryId;
    private String categoryName;
    private Image categoryBanner;
    private Image categoryImage;
    private List<SubCategoryDTO> subCategories;
}
