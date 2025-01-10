package com.zodo.kart.repository.users.operator;

import com.zodo.kart.entity.users.DeliveryPerson;
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
public interface DeliveryPersonRepository  extends JpaRepository<DeliveryPerson,Long> {

    @Query("SELECT d FROM DeliveryPerson d JOIN d.personalDetails o WHERE o.email = :email")
    Optional<DeliveryPerson> findByEmail(String email);

    @Query("SELECT d FROM DeliveryPerson d WHERE d.personalDetails.roles = :role")
    List<DeliveryPerson> findDeliveryPersonsByRole(@Param("role") String role);
}