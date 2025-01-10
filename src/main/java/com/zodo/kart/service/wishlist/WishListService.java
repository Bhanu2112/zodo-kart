package com.zodo.kart.service.wishlist;

import com.zodo.kart.dto.inventory.InventoryProductDto;
import com.zodo.kart.dto.inventory.ProductResponse;
import com.zodo.kart.dto.wishlist.WProductsDto;
import com.zodo.kart.dto.wishlist.WishListDto;
import com.zodo.kart.entity.inventory.InventoryProduct;
import com.zodo.kart.entity.wishlist.WProducts;
import com.zodo.kart.entity.wishlist.WishList;
import com.zodo.kart.exceptions.ItemNotFoundException;
import com.zodo.kart.repository.wishlist.WProductRepository;
import com.zodo.kart.repository.wishlist.WishListRepository;
import com.zodo.kart.service.cart.CartService;
import com.zodo.kart.service.inventory.InventoryServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Author : Bhanu prasad
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class WishListService {

    private final WishListRepository wishListRepository;
    private final WProductRepository wProductRepository;
    private final InventoryServiceImpl inventoryService;
  //  private final CartService cartService;


    // add to wish list

    public String addToWishList(Long customerId, Long prodId, Long packId) {
        Optional<WishList> wishList = wishListRepository.findByCustomerId(customerId);
        WishList w = wishList.orElse(null);
        if(wishList.isEmpty()) {
            w = new WishList();
            w.setCustomerId(customerId);
          w =  wishListRepository.save(w);
        }

        try{
            InventoryProduct inventoryProduct = new InventoryProduct();
            inventoryProduct.setIpId(prodId);
            WProducts wProduct = new WProducts();
            wProduct.setProduct(inventoryProduct);
            wProduct.setPackId(packId);
            wProduct.setWishList(w);
            w.getProducts().add(wProduct);
            wishListRepository.save(w);
            return "Added to wish list";
        }catch (Exception e) {
            throw new RuntimeException("Something went wrong");
        }

    }


//    public String removeProductFromWishList(Long customerId, Long ipId){
//        WishList wishList = wishListRepository.findByCustomerId(customerId)
//                .orElseThrow(() -> new RuntimeException("Wish list not found"));
//
//       WProducts product =  wishList.getProducts().stream().filter(wp -> wp.getProduct().getIpId()==ipId).findFirst().get();
//        deleteWProduct(product.getWpId());
//        return null;
//    }

    // remove from wish list
//
//    public String removeFromWishList(Long customerId, Long ipId) {
//        Logger logger = Logger.getLogger("WishListService");
//
//
//        WishList wishList = wishListRepository.findByCustomerId(customerId)
//                .orElseThrow(() -> new RuntimeException("Wish list not found"));
//
//        WProducts product = wishList.getProducts().stream()
//                .filter(wProducts -> wProducts.getProduct().getIpId().equals(ipId))
//                .findFirst()
//                .orElseThrow(() -> new ItemNotFoundException("Product not found in wishlist"));
//
//        try{
//           deleteWProduct(product.getWpId());
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//       return null;
//    }




    // move to cart


    public String deleteWProduct(Long wpId){
        try{
            wProductRepository.deleteById(wpId);
            return "Product removed from wishlist ";
        }catch (Exception e){
            return "Unable to process";
        }


    }


    public String getWp(Long ipId, Long customerId){
        WishList wishList = wishListRepository.findByCustomerId(customerId).orElseThrow(() -> new RuntimeException("Wish list not found"));

        Logger logger = Logger.getLogger("WishListService");

        WProducts product = wishList.getProducts().stream().filter(wProducts -> wProducts.getProduct().getIpId() == ipId).findFirst().orElseThrow(() -> new ItemNotFoundException("product not found in wishlist"));
        if (product != null && product.getWpId() != null) {
            logger.info("Deleting product with ID: " + product.getWpId());
            wProductRepository.deleteById(product.getWpId());
            log.warn(String.valueOf(product));
            return "Removed from wish list";
        } else {
            logger.warning("Product not found in wish list or has a null ID.");
        }
        return "Not found";
    }

    // get customer wish list
    public WishListDto getCustomerWishList(Long customerId) {

        WishList wishList = wishListRepository.
                findByCustomerId(customerId).
                orElseThrow(() -> new RuntimeException("Wish list not found"))
                ;
        return convertWishListToWishListDto(wishList);
    }


    // add wishlist product to cart

//    public String addWishListProductToCart(Long customerId, Long prodId) {
//        Optional<WishList> wishList = wishListRepository.findByCustomerId(customerId);
//        wishList.get().getProducts().forEach(wProducts -> {
//            if(wProducts.getProduct().getIpId() == prodId) {
//                cartService.addToCart(customerId, prodId,1);
//                wProductRepository.deleteById(wProducts.getWpId());
//            }
//        });
//        return "Added to cart";
//
//    }

    // get wishlist products by customer id

    public List<ProductResponse> getWishListProductsByCustomerId(Long customerId) {
        return wishListRepository.findByCustomerId(customerId).
                orElseThrow(() -> new RuntimeException("Wish list not found")).
                getProducts().
                stream().
                map(ip -> convertInventoryProductToProductResponse(ip.getProduct())).
                toList();
    }


    // convert inventory product to product response

    // convert inventory product to inventory product dto and next product response


    public ProductResponse convertInventoryProductToProductResponse(InventoryProduct inventoryProduct) {
        InventoryProductDto inventoryProductDto =  inventoryService.convertInventoryProductToInventoryProductDto(inventoryProduct);
        ProductResponse productResponse = inventoryService.convertInventoryProductDtoToProductResponse(inventoryProductDto);
        return productResponse;
    }


    // convert wishlist to wishlist dto

    public WishListDto convertWishListToWishListDto(WishList wishList) {
        return WishListDto.builder()
                .wishListId(wishList.getWishListId())
                .customerId(wishList.getCustomerId())
                .products(wishList.getProducts().stream().map(this::convertWProductToWProductDto).toList())
                .build();
    }

    // convert Wproduct to Wproduct dto

    public WProductsDto convertWProductToWProductDto(WProducts wProducts) {
        return WProductsDto.builder()
                .wpId(wProducts.getWpId())
                .product(convertInventoryProductToProductResponse(wProducts.getProduct()))
                .packId(wProducts.getPackId())
                .build();
    }


}
