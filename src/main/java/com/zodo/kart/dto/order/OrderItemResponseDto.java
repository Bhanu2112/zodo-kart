package com.zodo.kart.dto.order;

import com.zodo.kart.dto.inventory.ProductResponse;
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
public class OrderItemResponseDto {

    private Long orderItemId;
    private ProductResponse product;
    private Long packId;
    private int quantity;
    private double price;
}
