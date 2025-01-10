package com.zodo.kart.service.cart;

import com.zodo.kart.dto.cart.CartDto;
import com.zodo.kart.dto.cart.CartItemDto;
import com.zodo.kart.dto.inventory.InventoryProductDto;
import com.zodo.kart.dto.inventory.ProductResponse;
import com.zodo.kart.entity.cart.Cart;
import com.zodo.kart.entity.cart.CartItem;
import com.zodo.kart.entity.cart.SellerCart;
import com.zodo.kart.entity.cart.SellerCartItem;
import com.zodo.kart.entity.inventory.IPPack;
import com.zodo.kart.entity.inventory.InventoryProduct;
import com.zodo.kart.entity.wishlist.WishList;
import com.zodo.kart.repository.cart.CartItemRepository;

import com.zodo.kart.repository.cart.CartRepository;
import com.zodo.kart.repository.wishlist.WProductRepository;
import com.zodo.kart.repository.wishlist.WishListRepository;
import com.zodo.kart.service.inventory.InventoryService;
import com.zodo.kart.service.inventory.InventoryServiceImpl;
import com.zodo.kart.service.wishlist.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Author : Bhanu prasad
 */

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;

    private final CartRepository cartRepository;

    private final WishListService wishListService;

    private final WishListRepository wishListRepository;
    private final WProductRepository wProductRepository;

    private final InventoryServiceImpl inventoryServiceimpl;

    public String addToCart(Long customerId, Long prodId, Long packId, int quantity) {
        Optional<Cart> cart = cartRepository.findById(customerId);
        Cart c = cart.orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setCustomerId(customerId);
            newCart.setTotalAmount(0);
            return newCart;
        });

        // Check if cartItems is null, initialize if necessary
        if (c.getCartItems() == null) {
            c.setCartItems(new ArrayList<>());
        }

        // Create and configure CartItem
        CartItem cartItem = new CartItem();
        cartItem.setPackId(packId);
        cartItem.setQuantity(quantity);
        cartItem.setCart(c);
        cartItem.setProduct(inventoryServiceimpl.getInventoryProductById(prodId));

        // Add CartItem to cart's list and save
        c.getCartItems().add(cartItem);
        cartRepository.save(c);

        // Calculate and save total amount
        calculateCartTotal(customerId);

        return "Product added to cart successfully";
    }

//    public String addToCart(Long customerId, Long prodId, Long packId, int quantity) {
//        Optional<Cart> cart = cartRepository.findById(customerId);
//        Cart c = cart.orElse(null);
//
//        if(c==null){
//            c= new Cart();
//            c.setCustomerId(customerId);
//            c.setTotalAmount(0);
//
//            List<CartItem> cartItems = new ArrayList<>();
//
//            CartItem cartItem = new CartItem();
//            cartItem.setPackId(packId);
//            cartItem.setQuantity(quantity);
//            cartItem.setCart(c);
//            cartItem.setProduct(inventoryServiceimpl.getInventoryProductById(prodId));
//            cartItems.add(cartItem);
//
//
//            c.setCartItems(cartItems);
//            cartRepository.save(c);
//
//            c.setCartItems(null);
//            cartRepository.save(c);
//
//            calculateCartTotal(customerId);
//            return "Product added to cart successfully";
//
//        }else {
//            List<CartItem> cartItems = new ArrayList<>();
//            if(!c.getCartItems().isEmpty()) {
//                cartItems = c.getCartItems();
//            }
//
//
//        CartItem cartItem = new CartItem();
//        cartItem.setPackId(packId);
//        cartItem.setQuantity(quantity);
//        cartItem.setCart(c);
//        cartItem.setProduct(inventoryServiceimpl.getInventoryProductById(prodId));
//        cartItems.add(cartItem);
//        c.setCartItems(cartItems);
//        cartRepository.save(c);
//        calculateCartTotal(customerId);
//        return "Product added to cart successfully";
//        }
//
//    }


    public List<CartItem> getCartItems(Long customerId) {
        Optional<Cart> cart = cartRepository.findById(customerId);
        Cart c = cart.orElse(null);
        if(cart.isEmpty()) {
            return null;
        }else{
            return c.getCartItems();
        }
    }

    // delete cart item by cart item id
    public String deleteCartItem(Long customerId, Long cartItemId) {
       try{
           cartItemRepository.deleteById(cartItemId);
           calculateCartTotal(customerId);
           return "Item deleted successfully";
       }catch (Exception e){
           return "Item not deleted";
       }
    }

    // update quantity of cart item by cart item id and update modified date and type is add or remove

    public String updateCartItem(Long customerId, Long cartItemId, int quantity, String type) {
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);
        CartItem c = cartItem.orElse(null);
        if(cartItem.isEmpty()) {
            return null;
        }else{
            if(type.equals("add")) {
                c.setQuantity(c.getQuantity() + quantity);
            }else {
                c.setQuantity(c.getQuantity() - quantity);
            }

            cartItemRepository.save(c);
            calculateCartTotal(customerId);
            return "Item quantity updated successfully";
        }
    }


    /**
     * This method moves a product from cart to wish list by customer id and cart item id
     * @param customerId the customer id
     * @param cartItemId the cart item id
     * @return a string message
     */
    public String moveCartProductToWishList(Long customerId, Long cartItemId) {
        Optional<Cart> cart = cartRepository.findById(customerId);
        cart.get().getCartItems()
                .forEach(cartItem -> {
                    if(cartItem.getCartItemId().equals(cartItemId)) {
                        wishListService.addToWishList(customerId, cartItem.getProduct().getIpId(), cartItem.getPackId());
                        cartItemRepository.deleteById(cartItem.getCartItemId());
                    }
                });

        calculateCartTotal(customerId);
        return "Product moved to wish list successfully";

    }

    public String addWishListProductToCart(Long customerId, Long wpId) {
        Optional<WishList> wishList = wishListRepository.findByCustomerId(customerId);
        wishList.get().getProducts().forEach(wProducts -> {
            if(wProducts.getProduct().getIpId() == wpId) {
                addToCart(customerId, wpId, wProducts.getPackId(), 1);
                wProductRepository.deleteById(wProducts.getWpId());
            }
        });
        calculateCartTotal(customerId);
        return "Added to cart";

    }


    //  get all carts
    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }


    // get cart by customer id
    public CartDto getCartByCustomerId(Long customerId) {
        Optional<Cart> cart = cartRepository.findById(customerId);
       return cart.map(this::convertToCartDto).orElse(null);
    }

    public CartDto convertToCartDto(Cart cart) {
        List<CartItemDto> cartItemDtos = cart.getCartItems().stream().map(this::convertToCartItemDto).toList();

        return CartDto.builder()
                .cartId(cart.getCartId())
                .customerId(cart.getCustomerId())
                .cartItems(cartItemDtos)
                .build();
    }

    private CartItemDto convertToCartItemDto(CartItem cartItem) {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setCartItemId(cartItem.getCartItemId());
        cartItemDto.setQuantity(cartItem.getQuantity());
       // cartItemDto.setProduct(cartItem.getProduct());
        cartItemDto.setPackId(cartItem.getPackId());
        cartItemDto.setProduct(convertInventoryProductToProductResponse(cartItem.getProduct()));
        return cartItemDto;
    }


    // convert inventory product to inventory product dto and next product response


    public ProductResponse convertInventoryProductToProductResponse(InventoryProduct inventoryProduct) {
      InventoryProductDto inventoryProductDto =  inventoryServiceimpl.convertInventoryProductToInventoryProductDto(inventoryProduct);
      ProductResponse productResponse = inventoryServiceimpl.convertInventoryProductDtoToProductResponse(inventoryProductDto);
      return productResponse;
    }




    public void calculateCartTotal(Long customerId) {
        Cart cart = cartRepository.findByCustomerId(customerId).orElse(null);

        double total = 0;

        // Check if the sellerCart exists
        if (cart == null) {
            // return total;  // Return 0 if the sellerCart does not exist
        }

        // Iterate through the cart items and calculate the total
        for (CartItem item : cart.getCartItems()) {
            for (IPPack pack : item.getProduct().getProductAvailablePacks()) {
                // Find the pack that matches the packId of the item
                if (pack.getPackId()==item.getPackId()) {
                    total += pack.getFinalPackPrice() * item.getQuantity();  // Accumulate the total
                }
            }
        }

        cart.setTotalAmount(total);

        cartRepository.save(cart);

    }






}
