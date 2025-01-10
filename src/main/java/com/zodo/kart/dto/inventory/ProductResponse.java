package com.zodo.kart.dto.inventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Author : Bhanu prasad
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {

    private Long prodId;

    private IProductRDto product;
    // seller discount
    private double sellerDiscount;

    // private String sellerInventoryId;
    private boolean featured;
    private List<IPPackDto> productAvailablePacks;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
