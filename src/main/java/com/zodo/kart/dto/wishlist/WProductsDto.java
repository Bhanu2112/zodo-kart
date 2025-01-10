package com.zodo.kart.dto.wishlist;

import com.zodo.kart.dto.inventory.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author : Bhanu prasad
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WProductsDto {

    private Long wpId;
    private ProductResponse product;
    private Long packId;
}
