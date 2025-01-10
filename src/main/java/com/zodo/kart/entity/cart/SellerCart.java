package com.zodo.kart.entity.cart;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * Author : Bhanu prasad
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SELLER_CART")
@ToString
public class SellerCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;
    private String sellerId;
    private double totalAmount;
    @OneToMany(mappedBy = "sellerCart",cascade = CascadeType.ALL)
    private List<SellerCartItem> sellerCartItems;
}
