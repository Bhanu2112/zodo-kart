package com.zodo.kart.dto.inventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Author : Bhanu prasad
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IProductRDto {
   // private Long productId;
    private String productName;
    private ISubCategoryDto subcategory;
    private double price;
    private double generalDiscount;
    private Map<String, List<String>> attributes;
    private String sku;
}
