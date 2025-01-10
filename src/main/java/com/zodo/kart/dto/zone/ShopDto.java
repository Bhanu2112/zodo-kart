package com.zodo.kart.dto.zone;

import com.zodo.kart.entity.location.Coordinate;
import com.zodo.kart.entity.location.Zone;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Author : Bhanu prasad
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopDto {


    @NotBlank(message = "Shop name must not be empty")
    private String shopName;

    @NotBlank(message = "Owner name must not be empty")
    private String ownerName;

    @NotBlank(message = "GST number must not be empty")
    private String gstNumber;  // Indian GST registration number

    @NotBlank(message = "Shop address must not be empty")
    private String shopAddress;  // 12/34, Main Street, Building A, Near Park Plaza

    @NotBlank(message = "City must not be empty")
    private String city; // "Mumbai", "Delhi", "Bangalore"

    @NotBlank(message = "State must not be empty")
    private String state; // "Maharashtra", "Karnataka", "Tamil Nadu"

    @NotBlank(message = "Postal code must not be empty")
    private String postalCode;

    @NotBlank(message = "Phone number must not be empty")
    @Pattern(regexp = "^\\+?[0-9]{10}$", message = "Mobile number should be valid and contain 10 digits")
    private String phoneNumber;

    @NotBlank(message = "Email must not be empty")
    @Email(message = "Invalid email format")
    private String email;


    @NotBlank(message = "Image must not be empty")
    private String image;

    @NotNull(message = "Shop coordinate must not be empty")
    private CoordinateDto shopCoordinate;

    @NotNull(message = "Registration date must not be empty")
    private LocalDateTime registrationDate;

    @NotBlank(message = "Seller inventory id must not be empty")
    private String sellerInventoryId;

    @NotBlank(message = "Zone id must not be empty")
    private Long zoneId;
}
