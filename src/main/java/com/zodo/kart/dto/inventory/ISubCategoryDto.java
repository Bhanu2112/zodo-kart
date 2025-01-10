package com.zodo.kart.dto.inventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author : Bhanu prasad
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ISubCategoryDto {

    private Long subcategoryId;
    private String subcategoryName;
    private ImageDto subcategoryImage;
    private ICategoryDto category;
}