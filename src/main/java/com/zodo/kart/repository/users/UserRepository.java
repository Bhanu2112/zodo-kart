package com.zodo.kart.repository.users;

import com.zodo.kart.entity.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Author : Bhanu prasad
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByMobileNumber(String mobileNumber);
    Optional<User> findByEmail(String email);
}
