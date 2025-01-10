package com.zodo.kart.entity.product;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "SUB_CATEGORY")
public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subCategoryId;
    private String subCategoryName;
    @ManyToOne
    private Image subCategoryImage;


    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
