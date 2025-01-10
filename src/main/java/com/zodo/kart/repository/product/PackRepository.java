package com.zodo.kart.repository.product;

import com.zodo.kart.entity.product.Pack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Author : Bhanu prasad
 */

@Repository
public interface PackRepository extends JpaRepository<Pack, Long> {
}
