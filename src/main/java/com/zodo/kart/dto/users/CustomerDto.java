package com.zodo.kart.dto.users;

import com.zodo.kart.entity.users.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Author : Bhanu prasad
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CustomerDto {


    private Long id;


    private String name;

    private String nickName;


    private String email;


    private String mobileNumber;

    private String photoUrl;

    // private String roles; // CUSTOMER, SELLER, ADMIN, etc.,


    // private String status; // ACTIVE, INACTIVE, SUSPENDED


    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;



    private List<Address> addresses;

}
