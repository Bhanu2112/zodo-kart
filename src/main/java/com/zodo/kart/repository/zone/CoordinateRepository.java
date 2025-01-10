package com.zodo.kart.repository.zone;

import com.zodo.kart.entity.location.Coordinate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Author : Bhanu prasad
 */

@Repository
public interface CoordinateRepository extends JpaRepository<Coordinate, Long> {
}
