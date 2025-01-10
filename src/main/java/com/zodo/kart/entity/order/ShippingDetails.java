package com.zodo.kart.entity.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

/**
 * Author : Bhanu prasad
 */

@Entity
@Table(name = "shipping_details")
public class ShippingDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shippingDetailsId;

    @OneToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    @OneToOne
    @JoinColumn(name = "oorder_id")
    @JsonIgnore
    private OperatorOrder oorder;
}


/**
 * The purpose of a shipping details class would be to store information related to the shipping of a product or package.
 * This could include details such as the shipping carrier, tracking number, estimated delivery date, and any other relevant information.
 */