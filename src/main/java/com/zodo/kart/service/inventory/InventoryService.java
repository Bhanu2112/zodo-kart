package com.zodo.kart.service.inventory;

import com.zodo.kart.dto.inventory.InventoryDto;
import com.zodo.kart.dto.inventory.InventoryProductDto;
import com.zodo.kart.dto.inventory.ProductResponse;
import com.zodo.kart.entity.inventory.Inventory;
import com.zodo.kart.entity.inventory.InventoryProduct;

import java.util.List;
import java.util.Map;

public interface InventoryService {

   // seller activities and admin activities

    // create inventory by seller inventory id
    public InventoryDto createInventory(String sellerInventoryId);

    // get inventory by seller inventory id
    public Inventory getInventoryBySellerInventoryId(String sellerInventoryId);

    // add product to inventory by seller inventory id and product id
    public InventoryProductDto addProductToInventory(String sellerInventoryId, Long productId, Double sellerDiscount);

    // get inventory product by inventory product id
    public InventoryProductDto getInventoryProductByInventoryProductId(Long inventoryProductId);

    // get recently modified products by seller in last hours
    public List<InventoryProductDto> getRecentlyModifiedProductsBySellerInLastHours(String sellerInventoryId, int hours);

    // get recently created products by seller in last hours
    public List<InventoryProductDto> getRecentlyCreatedProductsBySellerInLastHours(String sellerInventoryId, int hours);

    // update pack quantity by pack id and update modified date and type is add or remove
    public String updatePackQuantityByPackId(Long packId,int quantity, String type);

    // get all category products of a particular seller
    public List<InventoryProductDto> getAllCategoryProductsOfASeller(String sellerInventoryId, String category) ;

    // get all subcategory products of a particular seller
    public List<InventoryProductDto> getAllSubcategoryProductsOfASeller(String sellerInventoryId, String subcategory);

    // group by category and subcategory
    public Map<String, Map<String, List<InventoryProductDto>>> groupByCategoryAndSubcategory (String sellerInventoryId);


    // update featured status of inventory product by inventory product id
    public InventoryProductDto updateFeaturedStatusByInventoryProductId(Long inventoryProductId);

    // apply discount to inventory products of a seller by seller inventory id and seller discount
    // it is a group discounts
    public List<InventoryProductDto> applyDiscountToInventoryProductsByCategoryWise(String sellerInventoryId, double sellerDiscount) ;

    // apply discount to individual inventory product
    public InventoryProductDto applyDiscountToInventoryProduct(Long inventoryProductId, double discount);


    // get all featured inventory products of a seller
    public List<InventoryProductDto> getAllFeaturedInventroyProductsOfASeller(String sellerInventoryId);



    /// customer activities

    // get single product by inventory product id( prod id)
    public ProductResponse getSingleProductByInventoryProductId(Long inventoryProductId);

    // get products of a sub category
    public List<ProductResponse> getProductsBySubCategory(String sellerInventoryId, String subCategory);


    // get all featured inventory products of a seller for customer
    public List<ProductResponse> getAllFeaturedInventoryProductsOfASellerForCustomer(String sellerInventoryId) ;

    public List<Inventory> getAllInventroies();

// get all category products for customer of a particular seller

    public List<ProductResponse> getAllCategoryProductsOfASellerForCustomer(String sellerInventoryId, String category);

    // get recently added sub category products

    public List<ProductResponse> getRecentlyAddedSubCategoryProductsForCustomer(String sellerInventoryId, String subCategory) ;


    // get all products of a seller for customer

    public List<ProductResponse> getAllProductsOfASellerForCustomer(String sellerInventoryId) ;

    // get inventory product by product id
    public InventoryProduct getInventoryProductById(Long inventoryProductId);

    }
