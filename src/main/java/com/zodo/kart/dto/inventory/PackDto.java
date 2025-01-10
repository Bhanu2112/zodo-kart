package com.zodo.kart.dto.inventory;

import com.zodo.kart.entity.product.Image;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class PackDto {

    private Long packId;

    private String packName; // 1kg,2kg,3kg

    private double packPrice; // 100,200,300

    private String packSku; // unique pack id for this pack



    private List<ImageDto> images;


}
