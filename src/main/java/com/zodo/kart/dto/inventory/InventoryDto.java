package com.zodo.kart.dto.inventory;

import com.zodo.kart.entity.inventory.InventoryProduct;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Author : Bhanu prasad
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryDto {

    private Long inventoryId;
    private String sellerInventoryId;
    private List<InventoryProductDto> inventoryProducts;
}
