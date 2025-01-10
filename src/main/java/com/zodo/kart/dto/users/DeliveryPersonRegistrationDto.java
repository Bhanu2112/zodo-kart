package com.zodo.kart.dto.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author : Bhanu prasad
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryPersonRegistrationDto {

    @NotBlank(message = "First Name must not be empty")
    private String firstName;
    private String lastName;
    private String nickName;
    @NotBlank(message = "Email must not be empty")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Mobile number cannot be blank")
    @Pattern(regexp = "^\\+?[0-9]{10}$", message = "Mobile number should be valid and contain 10 digits")
    private String mobileNumber;
    @NotBlank(message = "Password must not be empty")
    private String password;
    @NotBlank(message = "License Number must not be empty")
    private String licenceNumber;

    @NotBlank(message = "Vehicle Number must not be empty")
    private String vehicleNumber;

    @NotBlank(message = "Vehicle Type must not be empty")
    private String vehicleType;

    @NotBlank(message = "Vehicle Model must not be empty")
    private String vehicleModel;

    private String alternateMobileNumber;

    @NotBlank(message = "Vehicle Registration Number must not be empty")
    private String vehicleRegistrationNumber;

    @NotNull(message = "Seller ID must not be null")
    private Long sellerId;
}

