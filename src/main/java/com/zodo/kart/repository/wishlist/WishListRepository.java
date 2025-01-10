package com.zodo.kart.repository.wishlist;

import com.zodo.kart.entity.wishlist.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Author : Bhanu prasad
 */

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {

    Optional<WishList> findByCustomerId(Long customerId);
}
