package com.zodo.kart.service.zone;

import com.zodo.kart.entity.location.Coordinate;
import com.zodo.kart.entity.location.Zone;
import com.zodo.kart.repository.zone.CoordinateRepository;
import com.zodo.kart.repository.zone.ZoneRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Author : Bhanu prasad
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class ZoneService {

    private final ZoneRepository zoneRepository;
    private final CoordinateRepository coordinateRepository;


    // create zone

    @Transactional
    public Zone createZone(Zone zone) {
        log.info("[ZoneService:createZone] zone: {} ", zone);

        if (zone.getZoneCoordinates() != null) {
            for (Coordinate coordinate : zone.getZoneCoordinates()) {
                coordinate.setZone(zone);
            }
        }
        return zoneRepository.save(zone);
    }

    // update zone
    public Zone updateZone(Zone zone) {
        log.info("[ZoneService:updateZone] zone: {} ", zone);
        return zoneRepository.save(zone);
    }

    // get zone by seller id
    public Zone getZoneBySellerId(String sellerId) {
        log.info("[ZoneService:getZoneBySellerId] sellerId: {} ", sellerId);
        return zoneRepository.findByZoneSellerId(sellerId);
    }

    public List<Zone> getAllZones(){
        return zoneRepository.findAll();
    }

    public Zone assignZoneToSeller(Long zoneId, String sellerInventoryId){
        Zone zone = zoneRepository.findById(zoneId).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Not found"));

        zone.setZoneSellerId(sellerInventoryId);
        return zoneRepository.save(zone);

    }
}
