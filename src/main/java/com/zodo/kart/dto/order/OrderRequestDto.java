package com.zodo.kart.dto.order;

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
public class OrderRequestDto {

    private Long userId;
    private String sellerId;
    private double totalPrice;
    private Long shippingAddressId;
    private List<OrderItemRequestDto> orderItems;


}
