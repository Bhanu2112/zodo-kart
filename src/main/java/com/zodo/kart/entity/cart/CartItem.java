package com.zodo.kart.entity.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zodo.kart.entity.inventory.InventoryProduct;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author : Bhanu prasad
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CART_ITEM")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

//    private String sellerInventoryId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private InventoryProduct product;

    private Long packId;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonIgnore
    private Cart cart;



}
