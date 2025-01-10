package com.zodo.kart.entity.users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Author : Bhanu prasad
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OTP_STORE")
public class Otp {

    @Id
    @Column(name = "mobile_number", nullable = false, unique = true)
    private String mobileNumber;

    @Column(nullable = false)
    private int otp;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expiration_time", nullable = false)
    private LocalDateTime expirationTime;

}
