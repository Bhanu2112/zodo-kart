package com.zodo.kart.repository.users;

import com.zodo.kart.entity.users.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Author : Bhanu prasad
 */

@Repository
public interface OtpRepository extends JpaRepository<Otp, String> {
}
