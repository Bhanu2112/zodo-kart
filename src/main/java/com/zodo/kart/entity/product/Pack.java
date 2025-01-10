package com.zodo.kart.entity.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : Bhanu prasad
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PACK")
public class Pack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long packId;
    @NotBlank(message = "Pack name must not be empty")
    private String packName; // 1kg,2kg,3kg
    @NotNull(message = "Pack price must not be empty")
    private double packPrice; // 100,200,300
    @NotBlank(message = "Pack SKU must not be empty")
    private String packSku; // unique pack id for this pack


    @OneToMany(mappedBy = "pack", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Image> images;



    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    // private List<Image> images; // we are not including any images in inventory only thumbnail we will use to display product image
}
