package com.zodo.kart.controller.inventory;

import com.zodo.kart.dto.inventory.InventoryDto;
import com.zodo.kart.dto.inventory.InventoryProductDto;
import com.zodo.kart.dto.users.ResponseDto;
import com.zodo.kart.entity.inventory.Inventory;
import com.zodo.kart.entity.inventory.InventoryProduct;
import com.zodo.kart.service.inventory.InventoryService;
import com.zodo.kart.service.inventory.InventoryServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Author : Bhanu prasad
 */

@RestController
@RequestMapping("/public/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory management end points", description = "Api's for managing inventory and inventory products for sellers")
public class InventoryController {

    private final InventoryService inventoryService;


    // create inventory by seller inventory id

    @Operation(summary = "Create an inventory for a seller", description = "Creates an inventory for the specified seller using the sellerInventoryId")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Inventory created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid sellerInventoryId provided")
    })
   // @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN','SCOPE_SUB_ADMIN','SCOPE_SELLER')")
    @PostMapping("/create/{sellerInventoryId}")
    public ResponseEntity<ResponseDto<InventoryDto>> createInventory(@PathVariable String sellerInventoryId) {

        ResponseDto<InventoryDto> responseDto = new ResponseDto<>("Inventory created successfully", inventoryService.createInventory(sellerInventoryId));
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // get inventory by seller inventory id

    @GetMapping("/get/{sellerInventoryId}")
    public ResponseEntity<Inventory> getInventoryBySellerInventoryId(@PathVariable String sellerInventoryId) {

        return new ResponseEntity<>( inventoryService.getInventoryBySellerInventoryId(sellerInventoryId), HttpStatus.OK);
    }

    // add product to inventory by seller inventory id and product id
  //  @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN','SCOPE_SUB_ADMIN','SCOPE_SELLER')")
    @PostMapping("/add-product/{sellerInventoryId}/{productId}")
    public ResponseEntity<ResponseDto<InventoryProductDto>> addProductToInventory(@PathVariable String sellerInventoryId, @PathVariable Long productId) {

        ResponseDto<InventoryProductDto> responseDto = new ResponseDto<>("Product added to inventory successfully", inventoryService.addProductToInventory(sellerInventoryId, productId,0.0));
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }


    // get inventory product by inventory product id
    @GetMapping("/get-product/{inventoryProductId}")
    public ResponseEntity<InventoryProductDto> getInventoryProductByInventoryProductId(@PathVariable Long inventoryProductId) {

        return new ResponseEntity<>(inventoryService.getInventoryProductByInventoryProductId(inventoryProductId), HttpStatus.OK);
    }


    // get recently modified products by seller in last hours
    @GetMapping("/get-recently-modified/{sellerInventoryId}/{hours}")
    public ResponseEntity<List<InventoryProductDto>> getRecentlyModifiedProductsBySellerInLastHours(@PathVariable String sellerInventoryId, @PathVariable int hours) {
        return new ResponseEntity<>(inventoryService.getRecentlyModifiedProductsBySellerInLastHours(sellerInventoryId, hours), HttpStatus.OK);
    }

    // get recently created products by seller in last hours
    @GetMapping("/get-recently-created/{sellerInventoryId}/{hours}")
    public ResponseEntity<List<InventoryProductDto>> getRecentlyCreatedProductsBySellerInLastHours(@PathVariable String sellerInventoryId, @PathVariable int hours) {

        return new ResponseEntity<>(inventoryService.getRecentlyCreatedProductsBySellerInLastHours(sellerInventoryId, hours), HttpStatus.OK);
    }

    // update pack quantity by pack id and update modified date and type is add or remove
   // @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN','SCOPE_SUB_ADMIN','SCOPE_SELLER')")
    @PutMapping("/update-pack-quantity/{packId}/{quantity}/{type}")
    public ResponseEntity<?> updatePackQuantityByPackId(@PathVariable Long packId, @PathVariable int quantity, @PathVariable String type) {
        return new ResponseEntity<>( inventoryService.updatePackQuantityByPackId(packId,quantity,type), HttpStatus.OK);
    }

    // get all category products of a particular seller
    @GetMapping("/get-all-category-products/{sellerInventoryId}/{category}")
    public ResponseEntity<List<InventoryProductDto>> getAllCategoryProductsOfASeller(@PathVariable String sellerInventoryId, @PathVariable String category) {

        return new ResponseEntity<>(inventoryService.getAllCategoryProductsOfASeller(sellerInventoryId,category), HttpStatus.OK);
    }

    // get all subcategory products of a particular seller
    @GetMapping("/get-all-subcategory-products/{sellerInventoryId}/{subcategory}")
    public ResponseEntity<List<InventoryProductDto>> getAllSubcategoryProductsOfASeller(@PathVariable String sellerInventoryId, @PathVariable String subcategory) {

        return new ResponseEntity<>(inventoryService.getAllSubcategoryProductsOfASeller(sellerInventoryId,subcategory), HttpStatus.OK);
    }

    // group by category and subcategory
    @GetMapping("/group-by-category-and-subcategory/{sellerInventoryId}")
    public ResponseEntity<Map<String, Map<String, List<InventoryProductDto>>>> groupByCategoryAndSubcategory(@PathVariable String sellerInventoryId) {
        return new ResponseEntity<>(inventoryService.groupByCategoryAndSubcategory(sellerInventoryId), HttpStatus.OK);
    }

    // update featured status of inventory product by inventory product id
   // @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN','SCOPE_SUB_ADMIN','SCOPE_SELLER')")
    @PutMapping("/update-featured-status/{inventoryProductId}")
    public ResponseEntity<ResponseDto<InventoryProductDto>> updateFeaturedStatusByInventoryProductId(@PathVariable Long inventoryProductId) {
        ResponseDto<InventoryProductDto> responseDto = new ResponseDto<>("Featured status updated successfully", inventoryService.updateFeaturedStatusByInventoryProductId(inventoryProductId));
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // apply discount to inventory products of a seller by seller inventory id and seller discount
    // it is a group discounts
   // @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN','SCOPE_SUB_ADMIN','SCOPE_SELLER')")
    @PutMapping("/apply-group-discount/{sellerInventoryId}/{sellerDiscount}")
    public ResponseEntity<ResponseDto<List<InventoryProductDto>>> applyDiscountToInventoryProductsByCategoryWise(@PathVariable String sellerInventoryId, @PathVariable Double sellerDiscount) {
        ResponseDto<List<InventoryProductDto>> responseDto = new ResponseDto<>("Discounts applied successfully to inventory products", inventoryService.applyDiscountToInventoryProductsByCategoryWise(sellerInventoryId,sellerDiscount));
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // apply discount to individual inventory product
   // @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN','SCOPE_SUB_ADMIN','SCOPE_SELLER')")
    @PutMapping("/apply-individual-discount/{inventoryProductId}/{individualDiscount}")
    public ResponseEntity<ResponseDto<InventoryProductDto>> applyDiscountToInventoryProduct(@PathVariable Long inventoryProductId, @PathVariable Double individualDiscount) {
        ResponseDto<InventoryProductDto> responseDto = new ResponseDto<>("Discount applied successfully to inventory product", inventoryService.applyDiscountToInventoryProduct(inventoryProductId,individualDiscount));
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    // get featured inventroy products by seller inventory id
    @GetMapping("/get-featured-inventory-products/{sellerInventoryId}")
    public ResponseEntity<List<InventoryProductDto>> getFeaturedInventoryProducts(@PathVariable String sellerInventoryId) {
        return new ResponseEntity<>(inventoryService.getAllFeaturedInventroyProductsOfASeller(sellerInventoryId), HttpStatus.OK);
    }

    @GetMapping("/all-inventories")
    public ResponseEntity<?> getAllInventories(){
        return ResponseEntity.ok(inventoryService.getAllInventroies());
    }



}