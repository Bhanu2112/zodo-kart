package com.zodo.kart.dto.order.sellerorders;

import com.zodo.kart.dto.order.OrderItemRequestDto;
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
public class SOrderRequestDto {

    private String operatorId;
    private String sellerId;
    private double totalPrice;
    private Long shopAddressId;
    private List<SOrderItemRequestDto> orderItems;

}
