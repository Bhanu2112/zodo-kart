package com.zodo.kart.repository.zone;

import com.zodo.kart.entity.location.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {
    Zone findByZoneSellerId(String sellerId);
}
