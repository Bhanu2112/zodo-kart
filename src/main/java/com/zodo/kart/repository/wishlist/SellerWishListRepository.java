package com.zodo.kart.repository.wishlist;

import com.zodo.kart.entity.wishlist.SellerWishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerWishListRepository extends JpaRepository<SellerWishList, Long> {

    Optional<SellerWishList> findBySellerId(String sellerId);
}
