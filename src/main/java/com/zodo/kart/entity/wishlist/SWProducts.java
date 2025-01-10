package com.zodo.kart.entity.wishlist;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zodo.kart.entity.inventory.InventoryProduct;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author : Bhanu prasad
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "SWPRODUCTS")
public class SWProducts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long swpId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private InventoryProduct product;

    private Long packId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "s_wish_list_id")
    @JsonIgnore
    private SellerWishList wishList;
}
