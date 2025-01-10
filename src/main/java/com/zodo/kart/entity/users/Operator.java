package com.zodo.kart.entity.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Author : Bhanu prasad
 */


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="OPERATOR",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "EMAIL"),
                @UniqueConstraint(columnNames = "MOBILE_NUMBER")
        })
public class Operator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "First Name cannot be blank")
    private String firstName;

    private String lastName;

    private String fullName;

    private String nickName;

    @NotBlank(message = "Mobile number cannot be blank")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number should be valid and contain 10 digits")
    @Column(unique = true)
    private String mobileNumber;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotBlank(message = "Role cannot be blank")
    private String roles;

    @NotBlank(message = "Status cannot be blank")
    private String status;

    private String photoUrl;

    @NotNull(message = "Created date cannot be blank")
    private LocalDateTime createdAt;

    @NotNull(message = "Updated date cannot be null")
    private LocalDateTime updatedAt;



    @OneToMany(mappedBy = "operator",cascade = CascadeType.ALL)
    private List<Address> addresses;




    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL,mappedBy = "personalInfo")
    private Admin admin;
    @JsonIgnore
    @OneToOne(mappedBy = "personalInfo",cascade = CascadeType.ALL)
    private SubAdmin subAdmin;
    @JsonIgnore
    @OneToOne(mappedBy = "personalInfo",cascade = CascadeType.ALL)
    private Seller seller;
    @JsonIgnore
    @OneToOne(mappedBy = "personalDetails",cascade = CascadeType.ALL)
    private DeliveryPerson deliveryPerson;



    @JsonIgnore
    @OneToMany(mappedBy = "operator", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RefreshToken> refreshTokens;



}