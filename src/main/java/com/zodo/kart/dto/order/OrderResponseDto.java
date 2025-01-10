package com.zodo.kart.dto.order;

import com.zodo.kart.entity.order.OrderItems;
import com.zodo.kart.entity.order.Payment;
import com.zodo.kart.entity.order.ShippingDetails;
import com.zodo.kart.entity.users.Address;
import com.zodo.kart.enums.OrderStatus;
import com.zodo.kart.enums.PaymentMethods;
import com.zodo.kart.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Author : Bhanu prasad
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDto {


    private Long orderId;
    private Long userId;
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

    private List<OrderItemResponseDto> orderItems;
}
