package com.zodo.kart.dto.cart;

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
public class SCartDto {   // not using now

    private Long cartId;
    private String sellerId;
    private double totalAmount;

    private List<SCartItemDto> cartItems;
}
