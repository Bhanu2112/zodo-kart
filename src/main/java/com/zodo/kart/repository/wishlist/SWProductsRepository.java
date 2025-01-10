package com.zodo.kart.repository.wishlist;

import com.zodo.kart.entity.wishlist.SWProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SWProductsRepository extends JpaRepository<SWProducts, Long> {
}
