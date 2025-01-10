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
public class ICategoryDto {

    private Long categoryId;
    private String categoryName;
    private ImageDto categoryImage;

}