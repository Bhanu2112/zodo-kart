package com.zodo.kart.entity.wishlist;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Author : Bhanu prasad
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "seller_wish_list")
public class SellerWishList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishListId;
    private String sellerId;
    @OneToMany(mappedBy = "wishList",cascade = CascadeType.ALL)
    private List<SWProducts> products;
}
