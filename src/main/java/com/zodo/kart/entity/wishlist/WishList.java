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
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "WISHLIST")
public class WishList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishListId;
    private Long customerId;
    @OneToMany(mappedBy = "wishList",cascade = CascadeType.ALL)
    private List<WProducts> products;
}
