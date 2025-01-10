package com.zodo.kart.entity.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zodo.kart.entity.inventory.InventoryProduct;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Author : Bhanu prasad
 */

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="SELLER_CART_ITEM")
@ToString
public class SellerCartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    @ManyToOne
    @JoinColumn(name = "inventory_product_id")
    private InventoryProduct inventoryProduct;
    private Long packId;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "seller_cart_id")
    @JsonIgnore
    private SellerCart sellerCart;
}
