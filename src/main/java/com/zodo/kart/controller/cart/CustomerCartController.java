package com.zodo.kart.controller.cart;

import com.zodo.kart.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Author : Bhanu prasad
 */

@RestController
@RequestMapping("/cart/customer")
@RequiredArgsConstructor
public class CustomerCartController {

    private final CartService cartService;

    // add to cart

    @PostMapping("/add/product")
    public ResponseEntity<?> addToCart(@RequestParam Long customerId, @RequestParam Long prodId, @RequestParam Long packId, @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.addToCart(customerId, prodId, packId, quantity));
    }

    // remove from cart

    @DeleteMapping("/remove/product/{customerId}/{cartItemId}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long customerId, @PathVariable Long cartItemId) {
        return ResponseEntity.ok(cartService.deleteCartItem(customerId, cartItemId));
    }

    // get customer cart

    @GetMapping("/get/{customerId}")
    public ResponseEntity<?> getCart(@PathVariable Long customerId) {
        return ResponseEntity.ok(cartService.getCartByCustomerId(customerId));
    }

    // move cart item to wishlist

    @PostMapping("/move/product/{customerId}/{cartItemId}")
    public ResponseEntity<?> moveCartProductToWishList(@PathVariable Long customerId, @PathVariable Long cartItemId){
        return ResponseEntity.ok(cartService.moveCartProductToWishList(customerId, cartItemId));
    }

    // update quantity of cart item by cart item id and update modified date and type is add or remove

    @PutMapping("/item/update-quantity")
    public ResponseEntity<?> updateCartItemQuantity(@RequestParam Long customerId,
                                                    @RequestParam Long cartItemId,
                                                    @RequestParam int quantity,
                                                    @RequestParam String type){
        return ResponseEntity.ok(cartService.updateCartItem(customerId, cartItemId, quantity, type));
    }


    @GetMapping("/carts/all")
    public ResponseEntity<?> allCarts(){
        return ResponseEntity.ok(cartService.getAllCarts());
    }
}
