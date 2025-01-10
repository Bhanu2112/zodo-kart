package com.zodo.kart.service.cart;

import com.zodo.kart.dto.cart.SCartRequestDto;
import com.zodo.kart.dto.wishlist.SWishListRequestDto;
import com.zodo.kart.entity.cart.CartItem;
import com.zodo.kart.entity.cart.SellerCart;
import com.zodo.kart.entity.cart.SellerCartItem;
import com.zodo.kart.entity.inventory.IPPack;
import com.zodo.kart.entity.inventory.InventoryProduct;
import com.zodo.kart.entity.wishlist.SWProducts;
import com.zodo.kart.entity.wishlist.SellerWishList;
import com.zodo.kart.exceptions.ItemNotFoundException;
import com.zodo.kart.repository.cart.SellerCartItemRepository;
import com.zodo.kart.repository.cart.SellerCartRepository;
import com.zodo.kart.repository.inventory.InventoryProductRepository;
import com.zodo.kart.repository.wishlist.SWProductsRepository;
import com.zodo.kart.repository.wishlist.SellerWishListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Author : Bhanu prasad
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class SellerCartService {

    private final SellerCartRepository sellerCartRepository;

    private final SellerCartItemRepository sellerCartItemRepository;

    private final InventoryProductRepository inventoryProductRepository;

    private final SellerWishListRepository sellerWishListRepository;
    private final SWProductsRepository swProductsRepository;


    // add to seller cart

    public String addToCart(SCartRequestDto sCartRequestDto) {

        Optional<SellerCart> sellerCart = sellerCartRepository.findBySellerId(sCartRequestDto.getSellerInventoryId());
        if(sellerCart.isPresent()){
            SellerCart sc = sellerCart.get();
            SellerCartItem cartItem = new SellerCartItem();
            InventoryProduct inventoryProduct = inventoryProductRepository.findById(sCartRequestDto.getIpId()).orElseThrow(() -> new ItemNotFoundException("Product not found"));
            cartItem.setInventoryProduct(inventoryProduct);
            cartItem.setPackId(sCartRequestDto.getPackId());
            cartItem.setQuantity(sCartRequestDto.getQuantity());
            cartItem.setSellerCart(sc);
            sc.getSellerCartItems().add(cartItem);
            sellerCartRepository.save(sc);
            calculateCartTotal(sc.getSellerId());
            return "Item added to cart successfully";
        }else{
            SellerCart sc = new SellerCart();
            sc.setSellerId(sCartRequestDto.getSellerInventoryId());
            List<SellerCartItem> sellerCartItems = new ArrayList<>();
            SellerCartItem cartItem = new SellerCartItem();

            InventoryProduct inventoryProduct = inventoryProductRepository.findById(sCartRequestDto.getIpId()).orElseThrow(() -> new ItemNotFoundException("Product not found"));
            cartItem.setInventoryProduct(inventoryProduct);
            cartItem.setPackId(sCartRequestDto.getPackId());
            cartItem.setQuantity(sCartRequestDto.getQuantity());
            cartItem.setSellerCart(sc);
            sellerCartItems.add(cartItem);
            sc.setSellerCartItems(sellerCartItems);
            sellerCartRepository.save(sc);
            calculateCartTotal(sc.getSellerId());
            return "Item added to cart successfully";
        }

    }



    // calculate cart total



    public double calculateSellerCartValue(String sellerId) {
        Optional<SellerCart> sellerCart = sellerCartRepository.findBySellerId(sellerId);
        if(sellerCart.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found");
        }else{
            SellerCart sc = sellerCart.get();

            log.info("cart id: {} ", sc.getCartId());
            log.info("initial cart value: {} ", sc.getTotalAmount());
            double total = 0;

            log.info("cart items size: {} ", sc.getSellerCartItems().size());
            for(SellerCartItem item:sc.getSellerCartItems()){

                log.info("cart item id: {} ", item.getCartItemId());
                for (IPPack pack : item.getInventoryProduct().getProductAvailablePacks()) {
                    // Find the pack that matches the packId of the item
                    log.info("pack id: {} ", pack.getPackId());
                    log.info("item pack id: {} ", item.getPackId());
                    if (pack.getPackId()==item.getPackId()) {
                        total += pack.getFinalPackPrice() * item.getQuantity();  // Accumulate the total

                        log.info("final cart value in loop : {} ", total);
                    }
                }

            }
            return total;
        }


    }
    public void calculateCartTotal(String sellerId) {

        SellerCart sc = sellerCartRepository.findBySellerId(sellerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));

        double total = calculateSellerCartValue(sellerId);

        sc.setTotalAmount(total);
        sellerCartRepository.save(sc);
    }




    // get seller cart

    public SellerCart getSellerCart(String sellerId) {
       SellerCart sellerCart = sellerCartRepository.findBySellerId(sellerId).orElse(null);
       if(sellerCart==null){
           SellerCart sellerCart1 = new SellerCart();
           sellerCart1.setSellerId(sellerId);
           return sellerCartRepository.save(sellerCart1);
       }
       return sellerCart;
    }


    // remove from cart

    public String removeFromCart(Long cartItemId, String sellerId) {

      try{
          sellerCartItemRepository.deleteById(cartItemId);
          calculateCartTotal(sellerId);
          return "Item removed from cart successfully";
      }catch (Exception e){
          return "Failed to remove item from cart";
      }
    }


    // move cart product to wishlist

    public String moveCartProductToWishList(String sellerId, Long cartItemId) {

        SWishListRequestDto sWishListRequestDto = new SWishListRequestDto();


        SellerCartItem sellerCartItem = sellerCartItemRepository.findById(cartItemId).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Product not found"));
        InventoryProduct inventoryProduct = sellerCartItem.getInventoryProduct();

        sWishListRequestDto.setPackId(sellerCartItem.getPackId());

        sWishListRequestDto.setSellerInventoryId(sellerId);
        sWishListRequestDto.setIpId(cartItemId);

        var output = addToSellerWishList(sWishListRequestDto);

        calculateCartTotal(sellerId);
        if(output.equals("Product added to wishlist successfully")){
            return "Product moved to wishlist successfully";
        }


        return "Failed to move product to wishlist";
    }

    public String addToSellerWishList(SWishListRequestDto sWishListRequestDto){
        Optional<SellerWishList> sellerWishList = sellerWishListRepository.findBySellerId(sWishListRequestDto.getSellerInventoryId());
        SellerWishList wishList = sellerWishList.get();
        List<SWProducts> swProducts = wishList.getProducts();
        InventoryProduct inventoryProduct = inventoryProductRepository.findById(sWishListRequestDto.getIpId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Product not found"));

        SWProducts swProducts1 = new SWProducts();
        swProducts1.setProduct(inventoryProduct);
        swProducts1.setPackId(sWishListRequestDto.getPackId());

        swProducts1.setWishList(wishList);

        swProducts.add(swProductsRepository.save(swProducts1));



        wishList.setProducts(swProducts);
        sellerWishListRepository.save(wishList);


        return "Product added to wishlist successfully";
    }


    // update seller cart product quantity

    public String updateProductQuantityInCart(
        String sellerId, Long cartItemId, int quantity, String type) {
            Optional<SellerCartItem> cartItem = sellerCartItemRepository.findById(cartItemId);
            SellerCartItem c = cartItem.orElse(null);
            if(cartItem.isEmpty()) {
                return null;
            }else{
                if(type.equals("add")) {
                    c.setQuantity(c.getQuantity() + quantity);
                }else {
                    c.setQuantity(c.getQuantity() - quantity);
                }

                sellerCartItemRepository.save(c);
                calculateCartTotal(sellerId);
                return "Item quantity updated successfully";
            }
        }

        public String updateCartItemPack(String sellerId, Long packId, Long cartItemId) {
          SellerCartItem cartItem = sellerCartItemRepository.findById(cartItemId).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Product not found"));
          cartItem.setPackId(packId);
          sellerCartItemRepository.save(cartItem);
          calculateCartTotal(sellerId);
          return "Item pack updated successfully";
        }





}
