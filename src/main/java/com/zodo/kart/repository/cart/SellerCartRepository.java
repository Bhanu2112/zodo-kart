package com.zodo.kart.repository.cart;

import com.zodo.kart.entity.cart.SellerCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerCartRepository extends JpaRepository<SellerCart, Long> {


    Optional<SellerCart> findBySellerId(String sellerId);
}
