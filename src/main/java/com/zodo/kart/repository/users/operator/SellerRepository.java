package com.zodo.kart.repository.users.operator;

import com.zodo.kart.entity.users.Seller;
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
public interface SellerRepository extends JpaRepository<Seller,Long> {

    @Query("SELECT s FROM Seller s JOIN s.personalInfo o WHERE o.email = :email")
    Optional<Seller> findByEmail(String email);

    Optional<Seller> findByRegion(String region);

    @Query("SELECT s FROM Seller s WHERE s.personalInfo.roles = :role")
    List<Seller> findSellersByRole(@Param("role") String role);
}
