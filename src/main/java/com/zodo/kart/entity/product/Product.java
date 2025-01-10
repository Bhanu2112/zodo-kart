package com.zodo.kart.entity.product;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zodo.kart.entity.inventory.InventoryProduct;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author : Bhanu prasad
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PRODUCTS")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String productName;

    @ManyToOne
    @JoinColumn(name = "sub_category_id")
    private SubCategory subcategory;


    private double price;
    private double generalDiscount;

    @Lob // For storing large data, typically for images
    @ElementCollection // Use this to store a collection of images
    private Map<String, List<String>> dynamicAttributes = new HashMap<>(); // <key, value> key = title of the attribute, value = list of values


    private String sku; // generated value when adding new product


    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private List<Pack> availablePacks;



}
