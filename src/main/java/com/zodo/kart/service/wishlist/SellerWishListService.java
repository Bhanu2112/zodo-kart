package com.zodo.kart.service.wishlist;

import com.zodo.kart.dto.cart.SCartRequestDto;
import com.zodo.kart.dto.wishlist.SWishListRequestDto;
import com.zodo.kart.entity.inventory.InventoryProduct;
import com.zodo.kart.entity.wishlist.SWProducts;
import com.zodo.kart.entity.wishlist.SellerWishList;
import com.zodo.kart.repository.inventory.InventoryProductRepository;
import com.zodo.kart.repository.wishlist.SWProductsRepository;
import com.zodo.kart.repository.wishlist.SellerWishListRepository;
import com.zodo.kart.service.cart.SellerCartService;
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
public class SellerWishListService {

    private final SellerWishListRepository sellerWishListRepository;

    private final SWProductsRepository swProductsRepository;

    private final InventoryProductRepository inventoryProductRepository;

    private final SellerCartService sellerCartService;


    public String addToSellerWishList(SWishListRequestDto sWishListRequestDto) {

        Optional<SellerWishList> sellerWishList = sellerWishListRepository.findBySellerId(sWishListRequestDto.getSellerInventoryId());
        if(sellerWishList.isEmpty()) {

            SellerWishList wishList = new SellerWishList();
            wishList.setSellerId(sWishListRequestDto.getSellerInventoryId());

            sellerWishListRepository.save(wishList);
            List<SWProducts> swProducts = new ArrayList<>();
           InventoryProduct inventoryProduct = inventoryProductRepository.findById(sWishListRequestDto.getIpId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Product not found"));

           SWProducts swProducts1 = new SWProducts();
           swProducts1.setProduct(inventoryProduct);
           swProducts1.setPackId(sWishListRequestDto.getPackId());

           swProducts1.setWishList(wishList);
            swProductsRepository.save(swProducts1);
            swProducts.add(swProducts1);

            wishList.setProducts(swProducts);
           sellerWishListRepository.save(wishList);
           return "Product added to wishlist";
        }else{
            SellerWishList wishList = sellerWishList.get();

            List<SWProducts> swProducts = wishList.getProducts();
            InventoryProduct inventoryProduct = inventoryProductRepository.findById(sWishListRequestDto.getIpId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Product not found"));

            SWProducts swProducts1 = new SWProducts();
            swProducts1.setProduct(inventoryProduct);
            swProducts1.setPackId(sWishListRequestDto.getPackId());

            swProducts1.setWishList(wishList);
            swProductsRepository.save(swProducts1);
            swProducts.add(swProducts1);

            wishList.setProducts(swProducts);
            sellerWishListRepository.save(wishList);
            return "Product added to wishlist";

        }

    }


    public List<SellerWishList> getAllWishLists() {
        return sellerWishListRepository.findAll();
    }


    // find wishlist by seller id

    public SellerWishList getSellerWishList(String sellerId){
        return sellerWishListRepository.findBySellerId(sellerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Wish list not found"));
    }

//    // remove product from wishlist
//
//    public SellerWishList removeWishListProduct(String sellerId, Long swpId){
//        SellerWishList wishList = sellerWishListRepository.findBySellerId(sellerId)
//                .stream()
//                .findFirst()
//                .map(wl ->{
//                    wl.getProducts().removeIf(p -> {
//                        if(p.getSwpId()==swpId) {
//                            swProductsRepository.deleteById(p.getSwpId()); // Delete the product
//                            return true; // Remove the product from the list
//                        }
//
//                        return false;
//                    });
//                    return sellerWishListRepository.save(wl);
//                }).orElse(null);
//
//        return null;
//    }



    public SWProducts getSWProduct(Long swpId){
        return swProductsRepository.findById(swpId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Product not found"));
    }

    // delete swproducts by byid
    public String deleteSWP(Long swpId){
        try{
            SWProducts swProducts = swProductsRepository.findById(swpId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Product not found"));
           swProducts.setProduct(null);
           swProducts.setWishList(null);
           swProductsRepository.save(swProducts);
            swProductsRepository.deleteById(swpId);

            return "Deleted";
        }catch (Exception e){
            return "not deleted";
        }
    }

    // move wish list single product to cart

    public String moveWishListProductToCart(String sellerId, Long swpId){

        SWProducts swProducts = swProductsRepository.findById(swpId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Product not found"));

        SCartRequestDto cartRequestDto = new SCartRequestDto();
        cartRequestDto.setIpId(swProducts.getProduct().getIpId());
        cartRequestDto.setPackId(swProducts.getPackId());
        cartRequestDto.setQuantity(1);
        cartRequestDto.setSellerInventoryId(sellerId);

         String output = sellerCartService.addToCart(cartRequestDto);

         if(output.equals("Item added to cart successfully")){
             deleteSWP(swpId);
             return "Product moved to cart successfully";
         }
        return "Failed to move product to cart";


    }


    // move all wish list products to cart

    public String moveAllWishListProductsToCart(String sellerId) {
        List<SWProducts> swProducts = sellerWishListRepository.findBySellerId(sellerId).get().getProducts();

        int count =0;

        for (SWProducts swProduct : swProducts) {
            SCartRequestDto cartRequestDto = new SCartRequestDto();
            cartRequestDto.setIpId(swProduct.getProduct().getIpId());
            cartRequestDto.setPackId(swProduct.getPackId());
            cartRequestDto.setQuantity(1);
            cartRequestDto.setSellerInventoryId(sellerId);
          String output =   sellerCartService.addToCart(cartRequestDto);

            if(output.equals("Item added to cart successfully")){
                count++;
            }
            swProductsRepository.deleteById(swProduct.getSwpId());
        }

        if(count==swProducts.size()){
            return "All products moved to cart successfully";
        }else{
            return "Failed to move few/all products to cart";
        }

    }





}
