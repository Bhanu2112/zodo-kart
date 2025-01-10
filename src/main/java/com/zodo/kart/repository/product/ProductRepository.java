package com.zodo.kart.repository.product;

import com.zodo.kart.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Author : Bhanu prasad
 */

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
