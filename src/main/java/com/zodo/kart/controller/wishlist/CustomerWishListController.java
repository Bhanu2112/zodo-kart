package com.zodo.kart.controller.wishlist;

import com.zodo.kart.service.cart.CartService;
import com.zodo.kart.service.wishlist.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Author : Bhanu prasad
 */

@RestController
@RequestMapping("/wishlist/customer")
@RequiredArgsConstructor
public class CustomerWishListController {

    private final WishListService wishListService;

    private final CartService cartService;

    // add to wishlist

    @PostMapping("/add/product/{customerId}/{prodId}/{packId}")
    public ResponseEntity<?> addToWishList(@PathVariable Long customerId, @PathVariable Long prodId, @PathVariable Long packId) {
        return ResponseEntity.ok(wishListService.addToWishList(customerId, prodId, packId));
    }

    // remove from wishlist

    @DeleteMapping("/remove/product/{wpId}")
    public ResponseEntity<?> removeFromWishList( @PathVariable Long wpId) {
        return ResponseEntity.ok(wishListService.deleteWProduct(wpId));
    }

    // get wishlist

    @GetMapping("/get/{customerId}")
    public ResponseEntity<?> getWishList(@PathVariable Long customerId) {
        return ResponseEntity.ok(wishListService.getCustomerWishList(customerId));
    }

    // move wishlist product to cart

    @PostMapping("/add/product/to/cart/{customerId}/{wpId}")
    public ResponseEntity<?> addWishListProductToCart(@PathVariable Long customerId, @PathVariable Long wpId) {
        return ResponseEntity.ok(cartService.addWishListProductToCart(customerId, wpId));
    }



}
