package com.zodo.kart.controller.cart;

import com.zodo.kart.dto.cart.SCartRequestDto;
import com.zodo.kart.service.cart.SellerCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Author : Bhanu prasad
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller/cart")
public class SellerCartController {

    private final SellerCartService sellerCartService;


    // add to cart

    @PostMapping("/add/product")
    public ResponseEntity<?> addToCart(@RequestBody SCartRequestDto sCartRequestDto) {
        return ResponseEntity.ok(sellerCartService.addToCart(sCartRequestDto));
    }

    // remove from cart

    @PostMapping("/remove/product/{sellerInventoryId}/{cartProductId}")
    public ResponseEntity<?> removeFromCart(@PathVariable String sellerInventoryId, @PathVariable Long cartProductId) {
        return ResponseEntity.ok(sellerCartService.removeFromCart(cartProductId, sellerInventoryId));
    }

    // move cart product to wishlist

    @PostMapping("/move/product/{sellerInventoryId}/{cartItemId}")
    public ResponseEntity<?> moveProductToWishList(@PathVariable String sellerInventoryId, @PathVariable Long cartItemId) {
        return ResponseEntity.ok(sellerCartService.moveCartProductToWishList(sellerInventoryId, cartItemId));
    }


    // get seller cart

    @GetMapping("/get/{sellerInventoryId}")
    public ResponseEntity<?> getSellerCart(@PathVariable String sellerInventoryId) {
        return ResponseEntity.ok(sellerCartService.getSellerCart(sellerInventoryId));
    }

   @PutMapping("/update/product-quantity")
   public ResponseEntity<?> updateProductQuantityInCart(
           @RequestParam String sellerId, @RequestParam Long cartItemId, @RequestParam int quantity, @RequestParam String type) {
        return ResponseEntity.ok(sellerCartService.updateProductQuantityInCart(sellerId,cartItemId, quantity, type));
   }



   @GetMapping("/total/{sellerId}")
   public double calculateCartTotal( @PathVariable String sellerId) {
       return sellerCartService.calculateSellerCartValue(sellerId);
   }

   @PutMapping("/update/product-pack/{sellerId}/{packId}/{cartItemId}")
   public String updateCartItemPack(@PathVariable String sellerId, @PathVariable Long packId, @PathVariable Long cartItemId) {
       return sellerCartService.updateCartItemPack(sellerId, packId, cartItemId);
   }

}
