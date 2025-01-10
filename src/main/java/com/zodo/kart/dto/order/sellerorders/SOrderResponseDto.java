package com.zodo.kart.dto.order.sellerorders;

import com.zodo.kart.dto.order.OrderItemResponseDto;
import com.zodo.kart.dto.order.SAddressDto;
import com.zodo.kart.entity.order.Payment;
import com.zodo.kart.entity.order.ShippingDetails;
import com.zodo.kart.enums.OrderStatus;
import com.zodo.kart.enums.PaymentMethods;
import com.zodo.kart.enums.PaymentStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class SOrderResponseDto {


    private Long orderId;
    private String operatorId;
    private String sellerId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // e.g., PENDING, COMPLETED, CANCELLED

    private double totalPrice;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus; // Status of payment (e.g., PENDING, SUCCESS, FAILED)

    @Enumerated(EnumType.STRING)
    private PaymentMethods paymentMethod; // Payment method used for the order (e.g., NETBANKING, UPI)

    private LocalDateTime orderDate;

    private Payment payment;

    private ShippingDetails shippingDetails;



    private SAddressDto shippingAddress;

    private List<SOrderItemResponseDto> orderItems;
}
