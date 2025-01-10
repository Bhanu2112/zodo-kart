package com.zodo.kart.controller.zone;

import com.zodo.kart.dto.zone.ShopDto;
import com.zodo.kart.entity.users.Shop;
import com.zodo.kart.service.zone.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Author : Bhanu prasad
 */

@RestController
@RequestMapping("/public/shop")
@RequiredArgsConstructor
public class ShopController {
    private final ShopService shopService;


    // creating shop instance in our platform for seller

    @PostMapping("/create")
    public ResponseEntity<Shop> createShop(@RequestBody ShopDto shop) {
        return ResponseEntity.ok(shopService.createShop(shop));
    }

    // verify and update shop status by shop id
    @PostMapping("/verify/{shopId}/{status}")
    public ResponseEntity<Shop> verifyShopStatus(@PathVariable Long shopId, @PathVariable String status) {
        return ResponseEntity.ok(shopService.verifyShop(shopId, status));
    }

    // assign zone to shop

    @PostMapping("/update-zone/{shopId}/{zoneId}")
    public ResponseEntity<Shop> updateShopZone(@PathVariable Long shopId, @PathVariable Long zoneId) {
        return ResponseEntity.ok(shopService.updateShopZone(shopId, zoneId));
    }

    // update shop

    @PostMapping("/update/{shopId}")
    public ResponseEntity<Shop> updateShop(@RequestBody ShopDto shopdto, @PathVariable Long shopId) {
        return ResponseEntity.ok(shopService.updateShop(shopdto, shopId));
    }

    // get shop by seller inventory id

    @GetMapping("/get-shop/{sellerInventoryId}")
    public ResponseEntity<Shop> getShopBySellerInventoryId(@PathVariable String sellerInventoryId) {
        return ResponseEntity.ok(shopService.getShopBySellerInventoryId(sellerInventoryId));
    }


    @GetMapping("/get-shops")
    public ResponseEntity<?> getAllShops(){
        return ResponseEntity.ok(shopService.getAllShops());
    }
}
