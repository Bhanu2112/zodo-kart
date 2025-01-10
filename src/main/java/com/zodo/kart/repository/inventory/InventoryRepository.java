package com.zodo.kart.repository.inventory;

import com.zodo.kart.entity.inventory.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Author : Bhanu prasad
 */

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long> {
    Optional<Inventory> findBySellerInventoryId(String sellerInventoryId);
}
