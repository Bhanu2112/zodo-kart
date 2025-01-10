package com.zodo.kart.entity.wishlist;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zodo.kart.entity.inventory.InventoryProduct;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author : Bhanu prasad
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "w_products")
public class WProducts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wpId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private InventoryProduct product;

    private Long packId;

    @ManyToOne
    @JoinColumn(name = "wish_list_id")
    @JsonIgnore
    private WishList wishList;

}
