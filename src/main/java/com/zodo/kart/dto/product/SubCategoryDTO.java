package com.zodo.kart.dto.product;

import com.zodo.kart.entity.product.Image;
import jakarta.persistence.ManyToOne;
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
public class SubCategoryDTO {
    private Long subCategoryId;
    private String subCategoryName;

    private Image subCategoryImage;

}
