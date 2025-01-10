package com.zodo.kart.repository.users.operator;

import com.zodo.kart.entity.users.SubAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Author : Bhanu prasad
 */

@Repository
public interface SubAdminRepository extends JpaRepository<SubAdmin, Long> {

    @Query("SELECT s FROM SubAdmin s JOIN s.personalInfo o WHERE o.email = :email")
    Optional<SubAdmin> findByEmail(String email);

    @Query("SELECT sa FROM SubAdmin sa WHERE sa.personalInfo.roles = :role")
    List<SubAdmin> findSubAdminsByRole(@Param("role") String role);
}

