package com.zodo.kart.dto.cart;


import com.zodo.kart.entity.inventory.InventoryProduct;
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
public class SCartItemDto { // not using as of now

    private Long cartItemId;

    private InventoryProduct product;

    private Long packId;

    private int quantity;
}
