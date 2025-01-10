package com.zodo.kart.controller.inventory;

import com.zodo.kart.dto.inventory.ProductResponse;
import com.zodo.kart.dto.users.ResponseDto;
import com.zodo.kart.service.inventory.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Author : Bhanu prasad
 */

@RestController
@RequestMapping("/public/inventory/product")
@RequiredArgsConstructor
public class IProductController {

    private final InventoryService inventoryService;





    // customer activities

    // get single product by inventory product id( prod id)

    @GetMapping("/get-product/{prodId}")
    public ResponseEntity<ResponseDto<ProductResponse>> getSingleProductByInventoryProductId(@PathVariable Long prodId){
        ResponseDto<ProductResponse> responseDto = new ResponseDto<>("Product fetched successfully", inventoryService.getSingleProductByInventoryProductId(prodId));
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // get products of a sub category

    @GetMapping("/get-products-by-subcategory/{sellerInventoryId}/{subCategory}")
    public ResponseEntity<ResponseDto<List<ProductResponse>>> getProductsBySubCategoryId(@PathVariable String sellerInventoryId , @PathVariable String subCategory){
        ResponseDto<List<ProductResponse>> responseDto = new ResponseDto<>("Products of subcategory fetched successfully", inventoryService.getProductsBySubCategory(sellerInventoryId,subCategory));
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // get all featured inventory products of a seller for customer

    @GetMapping("/get-featured-products/{sellerInventoryId}")
    public ResponseEntity<ResponseDto<List<ProductResponse>>> getFeaturedProductsBySellerInventoryId(@PathVariable String sellerInventoryId){
        ResponseDto<List<ProductResponse>> responseDto = new ResponseDto<>("Featured products fetched successfully", inventoryService.getAllFeaturedInventoryProductsOfASellerForCustomer(sellerInventoryId));
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    // get all category products for customer of a particular seller

    @GetMapping("/get-all-category-products/{sellerInventoryId}/{category}")
    public ResponseEntity<ResponseDto<List<ProductResponse>>> getAllCategoryProductsOfASeller(@PathVariable String sellerInventoryId, @PathVariable String category){
        ResponseDto<List<ProductResponse>> responseDto = new ResponseDto<>("Category products fetched successfully", inventoryService.getAllCategoryProductsOfASellerForCustomer(sellerInventoryId,category));
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // get recently added sub category products

    @GetMapping("/get-recently-added-products/{sellerInventoryId}/{SubCategory}")
    public ResponseEntity<ResponseDto<List<ProductResponse>>> getRecentlyAddedSubCategoryProducts(@PathVariable String sellerInventoryId, @PathVariable String SubCategory){
        ResponseDto<List<ProductResponse>> responseDto = new ResponseDto<>("Recently added products fetched successfully", inventoryService.getRecentlyAddedSubCategoryProductsForCustomer(sellerInventoryId,SubCategory));
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // get all products of a seller for customer

    @GetMapping("/get-all-products/{sellerInventoryId}")
    public ResponseEntity<ResponseDto<List<ProductResponse>>> getAllProductsOfASeller(@PathVariable String sellerInventoryId){
        ResponseDto<List<ProductResponse>> responseDto = new ResponseDto<>("Products fetched successfully", inventoryService.getAllProductsOfASellerForCustomer(sellerInventoryId));
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
