package com.zodo.kart.dto.wishlist;

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
public class SWishListRequestDto {
    private String sellerInventoryId;
    private Long ipId;
    private Long packId;
}
