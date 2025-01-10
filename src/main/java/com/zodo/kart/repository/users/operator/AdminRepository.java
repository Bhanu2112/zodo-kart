package com.zodo.kart.repository.users.operator;

import com.zodo.kart.entity.users.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Author : Bhanu prasad
 */

@Repository
public interface AdminRepository  extends JpaRepository<Admin,Long> {
    @Query("SELECT a FROM Admin a JOIN a.personalInfo o WHERE o.email = :email")
    Optional<Admin> findByEmail(String email);

    @Query("SELECT a FROM Admin a WHERE a.personalInfo.roles = :role")
    List<Admin> findAdminsByRole( String role);
}
