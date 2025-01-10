package com.zodo.kart.controller.wishlist;

import com.zodo.kart.dto.wishlist.SWishListRequestDto;
import com.zodo.kart.service.wishlist.SellerWishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Author : Bhanu prasad
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/seller/wishlist")
public class SellerWishListController {

    private final SellerWishListService sellerWishListService;

    // add to wish list
    @PostMapping("/add/product")
    public ResponseEntity<?> addToWishList(@RequestBody SWishListRequestDto sWishListRequestDto) {

        return ResponseEntity.ok(sellerWishListService.addToSellerWishList(sWishListRequestDto));
    }

    // get all wishlists

    @GetMapping("/all")
    public ResponseEntity<?> getAllWishList() {
        return ResponseEntity.ok(sellerWishListService.getAllWishLists());
    }

    // get seller wish list by seller inventory id

    @GetMapping("/seller/{sellerInventoryId}")
    public ResponseEntity<?> getSellerWishList(@PathVariable String sellerInventoryId){
        return ResponseEntity.ok(sellerWishListService.getSellerWishList(sellerInventoryId));
    }


    // move wish list single product to cart

    @PostMapping("/move/wishlist/product/{sellerInventoryId}/{swpId}")
    public ResponseEntity<?> moveWishListToCart( @PathVariable String sellerInventoryId, @PathVariable Long swpId) {
        return ResponseEntity.ok(sellerWishListService.moveWishListProductToCart(sellerInventoryId, swpId));
    }

    // move all wish list products to cart

    @PostMapping("/move/wishlist/all/{sellerInventoryId}")
    public ResponseEntity<?> moveAllWishListToCart( @PathVariable String sellerInventoryId) {
        return ResponseEntity.ok(sellerWishListService.moveAllWishListProductsToCart(sellerInventoryId));
    }

    // remove from wishlist
    @DeleteMapping("/delete/product/{swpId}")
    public ResponseEntity<?> deleteSWPById(@PathVariable Long swpId){
        return  ResponseEntity.ok(sellerWishListService.deleteSWP(swpId));
    }




}
