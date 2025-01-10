package com.zodo.kart.controller.zone;

import com.zodo.kart.entity.location.Zone;
import com.zodo.kart.service.zone.ZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Author : Bhanu prasad
 */

@RestController
@RequestMapping("/public/zone")
@RequiredArgsConstructor
public class ZoneController {

    private final ZoneService zoneService;

    //  create zone

    @PostMapping("/create")
    public ResponseEntity<Zone> createZone(@RequestBody Zone zone) {
        return ResponseEntity.ok(zoneService.createZone(zone));
    }

    @PostMapping("/update")
    public ResponseEntity<Zone> updateZone(@RequestBody Zone zone) {
        return ResponseEntity.ok(zoneService.createZone(zone));
    }
    // get zone by seller id

    @GetMapping("/get-zone/{sellerId}")
    public ResponseEntity<Zone> getZoneBySellerId(@PathVariable String sellerId) {
        return ResponseEntity.ok(zoneService.getZoneBySellerId(sellerId));
    }

    @GetMapping("/all-zones")
    public ResponseEntity<?> gatAllZones(){
        return ResponseEntity.ok(zoneService.getAllZones());
    }
}
