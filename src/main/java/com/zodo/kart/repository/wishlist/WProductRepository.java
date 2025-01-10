package com.zodo.kart.repository.wishlist;

import com.zodo.kart.entity.wishlist.WProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WProductRepository extends JpaRepository<WProducts, Long> {
}
