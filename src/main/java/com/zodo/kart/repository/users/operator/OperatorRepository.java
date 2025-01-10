package com.zodo.kart.repository.users.operator;

import com.zodo.kart.entity.users.Operator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Author : Bhanu prasad
 */

@Repository
public interface OperatorRepository  extends JpaRepository<Operator,Long> {

    Optional<Operator> findByEmail(String username);
}
