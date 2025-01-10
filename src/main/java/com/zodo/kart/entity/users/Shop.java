package com.zodo.kart.entity.users;

import com.zodo.kart.entity.location.Coordinate;
import com.zodo.kart.entity.location.Zone;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
* Author : Bhanu prasad
*/

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SHOPS")
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shopId;
    private String shopName;
    private String ownerName;
    private String gstNumber;  // Indian GST registration number


    private String shopAddress;  // 12/34, Main Street, Building A, Near Park Plaza
    private String city; // "Mumbai", "Delhi", "Bangalore"
    private String state; // "Maharashtra", "Karnataka", "Tamil Nadu"
    private String postalCode;
    private String phoneNumber;
    private String email;


    //private String shopType; // Example: Grocery, Meat, Vegetables & Fruits, etc.

    private boolean isVerified;
    private String image;


    @ManyToOne
    @JoinColumn(name="coordinate_id")
    private Coordinate shopCoordinate;

    @ManyToOne
    @JoinColumn(name = "zone_id")
    private Zone zone;

    private String status;    // Example: PENDING, ACTIVE, SUSPENDED
    private LocalDateTime registrationDate;


    private String sellerInventoryId;  // Link to Seller entity





}
