package com.zodo.kart.entity.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
@Table(name="USER_INFO")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    private String nickName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    @Column(name="email",unique = true)
    private String email;

    @NotBlank(message = "Mobile number cannot be blank")
    @Pattern(regexp = "^\\+?[0-9]{10}$", message = "Mobile number should be valid and contain 10 digits")
    @Column(name="mobile_number",unique = true)
    private String mobileNumber;

//    @NotBlank(message = "Password cannot be blank")
//  //  @Size(min = 8, message = "Password should be at least 8 characters long")
//    private String password;

    @NotBlank(message = "Role cannot be blank")
//    @Pattern(regexp = "CUSTOMER|SELLER|ADMIN", message = "Role must be one of CUSTOMER, SELLER, or ADMIN")
    private String roles; // CUSTOMER, SELLER, ADMIN, etc.,

    @NotBlank(message = "Status cannot be blank")
    @Pattern(regexp = "ACTIVE|INACTIVE|SUSPENDED", message = "Status must be one of ACTIVE, INACTIVE, or SUSPENDED")
    private String status; // ACTIVE, INACTIVE, SUSPENDED

    @NotNull(message = "Created date cannot be null")
    private LocalDateTime createdAt;

    @NotNull(message = "Updated date cannot be null")
    private LocalDateTime updatedAt;

    private String photoUrl;


    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Address> addresses;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RefreshToken> refreshTokens;


}
