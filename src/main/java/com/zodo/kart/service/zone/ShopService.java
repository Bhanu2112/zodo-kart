package com.zodo.kart.service.zone;

import com.zodo.kart.dto.zone.ShopDto;
import com.zodo.kart.entity.location.Coordinate;
import com.zodo.kart.entity.location.Zone;
import com.zodo.kart.entity.users.Shop;
import com.zodo.kart.enums.ShopStatus;
import com.zodo.kart.repository.zone.CoordinateRepository;
import com.zodo.kart.repository.zone.ShopRepository;
import com.zodo.kart.service.inventory.InventoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Author : Bhanu prasad
 */

@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;
    private final CoordinateRepository coordinateRepository;
    private final InventoryServiceImpl inventoryService;
    private final ZoneService zoneService;

    // creating shop instance in our platform for seller
    public Shop createShop(ShopDto shopDto) {
        Shop shop = new Shop();
        shop.setShopName(shopDto.getShopName());
        shop.setOwnerName(shopDto.getOwnerName());
        shop.setGstNumber(shopDto.getGstNumber());
        shop.setShopAddress(shopDto.getShopAddress());
        shop.setCity(shopDto.getCity());
        shop.setState(shopDto.getState());
        shop.setPostalCode(shopDto.getPostalCode());
        shop.setPhoneNumber(shopDto.getPhoneNumber());
        shop.setEmail(shopDto.getEmail());
        shop.setVerified(false);
        shop.setImage(shopDto.getImage());

        Coordinate coordinate = new Coordinate();

        coordinate.setLatitude(shopDto.getShopCoordinate().getLatitude());
        coordinate.setLongitude(shopDto.getShopCoordinate().getLongitude());

        coordinateRepository.save(coordinate);

        shop.setShopCoordinate(coordinate);

        shop.setStatus(ShopStatus.PENDING.name());
        shop.setRegistrationDate(shopDto.getRegistrationDate());
        shop.setSellerInventoryId(shopDto.getSellerInventoryId());


        Zone zone = zoneService.assignZoneToSeller(shopDto.getZoneId(), shop.getSellerInventoryId());

        shop.setZone(zone);

        return shopRepository.save(shop);
    }


    // verify and update shop status by shop id

    public Shop verifyShop(Long shopId , String status) {
        Shop shop = shopRepository.findById(shopId).get();

        if(status.equals(ShopStatus.VERIFIED.name())) {
            shop.setVerified(true);
            shop.setStatus(ShopStatus.VERIFIED.name());

            // create inventory for shop
            // inventoryService.createInventory(shop.getSellerInventoryId());


        }

        return shopRepository.save(shop);
    }

    // assign zone to shop
    public Shop updateShopZone(Long shopId, Long zoneId) {
        Shop shop = shopRepository.findById(shopId).get();
        shop.setZone(zoneService.assignZoneToSeller(zoneId,shop.getSellerInventoryId()));
        return shopRepository.save(shop);
    }

    public Shop updateShop(ShopDto shopdto, Long shopId) {
        Shop shop = shopRepository.findById(shopId).get();
        shop.setShopName(shopdto.getShopName());
        shop.setOwnerName(shopdto.getOwnerName());
        shop.setGstNumber(shopdto.getGstNumber());
        shop.setShopAddress(shopdto.getShopAddress());
        shop.setCity(shopdto.getCity());
        shop.setState(shopdto.getState());
        shop.setPostalCode(shopdto.getPostalCode());
        shop.setPhoneNumber(shopdto.getPhoneNumber());
        shop.setEmail(shopdto.getEmail());
     //   shop.setVerified(false);
        shop.setImage(shopdto.getImage());
       // shop.setStatus(ShopStatus.PENDING.name());
        shop.setRegistrationDate(shopdto.getRegistrationDate());
      //  shop.setSellerInventoryId(shopdto.getSellerInventoryId());
        return shopRepository.save(shop);
    }


    // get shop by seller inventory id
    public Shop getShopBySellerInventoryId(String sellerInventoryId) {
        return shopRepository.findBySellerInventoryId(sellerInventoryId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Shop not found"));
    }

    // get all shops

    public List<Shop> getAllShops(){
        return shopRepository.findAll();
    }





}
