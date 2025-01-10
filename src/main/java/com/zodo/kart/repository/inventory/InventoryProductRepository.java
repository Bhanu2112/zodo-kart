package com.zodo.kart.repository.inventory;

import com.zodo.kart.entity.inventory.InventoryProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Author : Bhanu prasad
 */

@Repository
public interface InventoryProductRepository extends JpaRepository<InventoryProduct, Long> {

    @Query("SELECT COUNT(ip) > 0 FROM InventoryProduct ip WHERE ip.inventory.sellerInventoryId = :sellerInventoryId AND ip.product.productId = :productId")
    boolean existsBySellerInventoryIdAndProductId(@Param("sellerInventoryId") String sellerInventoryId, @Param("productId") Long productId);


    @Query("SELECT ip FROM InventoryProduct ip WHERE ip.inventory.sellerInventoryId = :sellerInventoryId AND ip.updatedAt >= :dateLimit")
    List<InventoryProduct> findRecentlyModifiedProductsBySellerInLastHours(@Param("sellerInventoryId") String sellerInventoryId, @Param("dateLimit") LocalDateTime dateLimit); // find recently modified products by seller in last Hours


    @Query("SELECT ip FROM InventoryProduct ip WHERE ip.inventory.sellerInventoryId = :sellerInventoryId AND ip.createdAt > :dateLimit ORDER BY ip.updatedAt DESC")
    Optional<List<InventoryProduct>> findRecentlyCreatedProductsBySellerInLastHours(@Param("sellerInventoryId") String sellerInventoryId, @Param("dateLimit") LocalDateTime dateLimit);

    Optional<InventoryProduct> findByProduct_ProductId(Long productId);
}
