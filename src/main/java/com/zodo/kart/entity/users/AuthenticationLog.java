package com.zodo.kart.entity.users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Author : Bhanu prasad
 */

@Entity
@Table(name = "AUTHENTICATION_LOG")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthenticationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mobileNumber; // The mobile number used for authentication
    private boolean successful; // Whether the attempt was successful
    private LocalDateTime timestamp; // When the attempt occurred
}
