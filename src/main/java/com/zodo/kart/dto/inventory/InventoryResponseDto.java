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
public class InventoryResponseDto {

    private Long inventoryId;

    private String sellerId;

    private Long productId;
    private String productName;
    private String productThumbnail;
    private double productOriginalPrice;
    private double generalDiscount;

    private double sellerDiscount;

    private List<IPackDto> availablePacks;

    private int soldQuantity;

    private String category;
    private String subcategory;
    private boolean featured;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

