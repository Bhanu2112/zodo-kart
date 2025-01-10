package com.zodo.kart.entity.inventory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zodo.kart.entity.product.Pack;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author : Bhanu prasad
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "IP_PACK")
public class IPPack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long packId;

    @ManyToOne
    @JoinColumn(name = "product_pack_id")
    private Pack pack;

    //private String packName;// 1kg,2kg,3kg
   // private double packPrice; // 100,200,300
    private double finalPackPrice; // price after discounts
    private int packQuantity; // 1,2,3
  //  private String packSku;

    @ManyToOne
    @JoinColumn(name = "inventory_product_id")
    @JsonIgnore
    private InventoryProduct inventoryProduct;



    public void packPriceAfterDiscount(double sellerDiscount) {
        this.finalPackPrice = this.pack.getPackPrice() - (this.pack.getPackPrice() * sellerDiscount / 100);
    }
}
