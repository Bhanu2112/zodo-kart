package com.zodo.kart.repository.cart;

import com.zodo.kart.entity.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Author : Bhanu prasad
 */

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
}
