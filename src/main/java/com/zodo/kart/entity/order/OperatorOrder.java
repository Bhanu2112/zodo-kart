package com.zodo.kart.entity.order;

import com.zodo.kart.entity.users.Address;
import com.zodo.kart.enums.OrderStatus;
import com.zodo.kart.enums.PaymentMethods;
import com.zodo.kart.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Author : Bhanu prasad
 */

@Entity
@Table(name = "operator_orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperatorOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne
    @JoinColumn(name = "shipping_address_id")
    private Address shippingAddress;



    @OneToMany(mappedBy = "operatorOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItems> orderItems;


    @OneToOne(mappedBy = "operatorOrder", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Payment payment;

    @OneToOne(mappedBy = "oorder", cascade = CascadeType.ALL)
    private ShippingDetails shippingDetails;



}
