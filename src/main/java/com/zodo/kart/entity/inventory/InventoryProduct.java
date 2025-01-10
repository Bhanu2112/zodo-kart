package com.zodo.kart.entity.inventory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zodo.kart.entity.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Author : Bhanu prasad
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "INVENTORY_PRODUCT")
public class InventoryProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ipId;

    @ManyToOne
    @JoinColumn(name = "inventory_id")
    @JsonIgnore
    private Inventory inventory;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private double sellerDiscount;

    @OneToMany(mappedBy = "inventoryProduct",cascade = CascadeType.ALL)
    private List<IPPack> productAvailablePacks;

    private int soldQuantity;
    private boolean featured;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
