package com.zodo.kart.entity.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author : Bhanu prasad
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "ADDRESS")
public class Address {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String street;   // House number and street name
    private String locality; // Area or locality
    private String city;     // City name
    private String state;    // State name
    private String pinCode;  // 6-digit PIN code
    private String country;
    @Pattern(regexp = "^\\+?[0-9]{10}$", message = "Mobile number should be valid and contain 10 digits")
    private String mobileNumber;

    private boolean isDefault;
    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name="operator_id")
    @JsonIgnore
    private Operator operator;
}