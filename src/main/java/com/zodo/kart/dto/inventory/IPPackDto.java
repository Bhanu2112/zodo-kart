package com.zodo.kart.dto.inventory;

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
public class IPPackDto {
    private Long packId;
    private int packQuantity;
    private PackDto pack;
    private double finalPackPrice; // final price after discount

  //  private List<ImageDto> packImages;


    // images for pack will be set later
}
