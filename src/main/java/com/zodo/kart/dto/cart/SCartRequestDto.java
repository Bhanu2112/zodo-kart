package com.zodo.kart.dto.cart;

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
public class SCartRequestDto {

    private String sellerInventoryId;
    private Long ipId;
    private Long packId;
    private int quantity;
}
