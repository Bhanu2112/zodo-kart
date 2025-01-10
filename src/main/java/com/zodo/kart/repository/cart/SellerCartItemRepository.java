package com.zodo.kart.repository.cart;

import com.zodo.kart.entity.cart.SellerCartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Author : Bhanu prasad
 */

@Repository
public interface SellerCartItemRepository extends JpaRepository<SellerCartItem, Long> {
}
