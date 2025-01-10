package com.zodo.kart.service.inventory;

import com.zodo.kart.dto.inventory.*;
import com.zodo.kart.entity.inventory.IPPack;
import com.zodo.kart.entity.inventory.Inventory;
import com.zodo.kart.entity.inventory.InventoryProduct;
import com.zodo.kart.entity.order.Order;
import com.zodo.kart.entity.product.Image;
import com.zodo.kart.entity.product.Pack;
import com.zodo.kart.entity.product.Product;
import com.zodo.kart.enums.OrderStatus;
import com.zodo.kart.exceptions.ItemNotFoundException;
import com.zodo.kart.repository.inventory.IPPackRepository;
import com.zodo.kart.repository.inventory.InventoryProductRepository;
import com.zodo.kart.repository.inventory.InventoryRepository;
import com.zodo.kart.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Author : Bhanu prasad
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final InventoryProductRepository inventoryProductRepository;
    private final IPPackRepository iPPackRepository;
    private final InventoryRepository inventoryRepository;

    private final ProductRepository productRepository;




    //  create seller inventory by seller inventory id
    public InventoryDto createInventory(String sellerInventoryId) {
        log.info("[InventoryService:createInventory] started for new seller having seller inventory id: {} ",sellerInventoryId);
        Inventory inventory = new Inventory();
        inventory.setSellerInventoryId(sellerInventoryId);

       Optional<Inventory> optionalInventory = inventoryRepository.findBySellerInventoryId(sellerInventoryId);

       if(optionalInventory.isPresent()){
           throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Inventory already present");
       }

        log.info("[InventoryService:createInventory] created inventory for seller inventory id: {} ",sellerInventoryId);
        inventory =inventoryRepository.save(inventory);
        return convertInventoryToInventoryDto(inventory);
    }


    // get inventory by seller inventory id
    public Inventory getInventoryBySellerInventoryId(String sellerInventoryId) {
        log.info("[InventoryService:getInventoryBySellerInventoryId] started for seller having seller inventory id: {} ",sellerInventoryId);
        return inventoryRepository.findBySellerInventoryId(sellerInventoryId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Inventory not found"));
    }



    // add product to inventory by seller inventory id and product id
    public InventoryProductDto addProductToInventory(String sellerInventoryId, Long productId, Double sellerDiscount) {
        log.info("[InventoryService:addProductToInventory] started for new product having seller inventory id: {} and product id: {} ",sellerInventoryId,productId);
        Inventory inventory = getInventoryBySellerInventoryId(sellerInventoryId);
        InventoryProduct inventoryProduct = new InventoryProduct();

        // Check if the product already exists in the inventory
        boolean productExistsInInventory = inventoryProductRepository.existsBySellerInventoryIdAndProductId(sellerInventoryId, productId);

        if (productExistsInInventory) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Product already exists in inventory");
        }


        Product product = productRepository.findById(productId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Product not found"));


        inventoryProduct.setProduct(product);
        inventoryProduct.setInventory(inventory);
        inventoryProduct.setCreatedAt(LocalDateTime.now());
        inventoryProduct.setUpdatedAt(LocalDateTime.now());


        // available packs

        List<IPPack> productAvailablePacks = new ArrayList<>();

        InventoryProduct finalInventoryProduct = inventoryProduct;
        product.getAvailablePacks().forEach(pack -> {

            IPPack iPPack = new IPPack();
            iPPack.setPack(pack);


//            iPPack.setPackName(pack.getPackName());
//            iPPack.setPackPrice(pack.getPackPrice());
//            iPPack.setPackSku(pack.getPackSku());
//
//
//
// generate pack sku
           // iPPack.setImages(pack.getImages());
            iPPack.packPriceAfterDiscount(sellerDiscount); // final price after discount applied
            iPPack.setPackQuantity(0); // by default pack quantity is set to 0
            iPPack.setInventoryProduct(finalInventoryProduct);
            productAvailablePacks.add(iPPack);
        });

        inventoryProduct.setProductAvailablePacks(productAvailablePacks);

        inventoryProduct = inventoryProductRepository.save(inventoryProduct);
        log.info("[InventoryService:addProductToInventory] added product to inventory with seller inventory id: {} and product id: {} ",sellerInventoryId,productId);
        return convertInventoryProductToInventoryProductDto(inventoryProduct);
    }


    // get inventory product by inventory product id
    public InventoryProductDto getInventoryProductByInventoryProductId(Long inventoryProductId) {
        InventoryProduct inventoryProduct = inventoryProductRepository.findById(inventoryProductId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Inventory product not found"));
      return convertInventoryProductToInventoryProductDto(inventoryProduct);
    }


    // get recently modified products by seller in last hours
    public List<InventoryProductDto> getRecentlyModifiedProductsBySellerInLastHours(String sellerInventoryId, int hours) {
        log.info("[InventoryService:getRecentlyModifiedProductsBySellerInLastHours] started for seller having seller inventory id: {} ",sellerInventoryId);
        LocalDateTime dateLimit = LocalDateTime.now().minusHours(hours);

        List<InventoryProduct> inventoryProducts = inventoryProductRepository.findRecentlyModifiedProductsBySellerInLastHours(sellerInventoryId, dateLimit);

        if (inventoryProducts.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No inventory products found");
        }
        return inventoryProducts.stream().map(this::convertInventoryProductToInventoryProductDto).collect(Collectors.toList());
    }


    // get recently created products by seller in last hours
    public List<InventoryProductDto> getRecentlyCreatedProductsBySellerInLastHours(String sellerInventoryId, int hours) {
        log.info("[InventoryService:getRecentlyCreatedProductsBySellerInLastHours] started for seller having seller inventory id: {} ",sellerInventoryId);
        LocalDateTime dateLimit = LocalDateTime.now().minusHours(hours);
        List<InventoryProduct> inventoryProducts = inventoryProductRepository.findRecentlyCreatedProductsBySellerInLastHours(sellerInventoryId,dateLimit).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"No inventory products found"));
        return inventoryProducts.stream().map(this::convertInventoryProductToInventoryProductDto).collect(Collectors.toList());
    }


    // update pack quantity by pack id and update modified date and type is add or remove

    public String updatePackQuantityByPackId(Long packId,int quantity, String type){
        log.info("[InventoryService:updatePackQuantityByPackId] started for pack having pack id: {} ",packId);
        iPPackRepository.findById(packId).map(iPPack -> {

            if(type.equals("add")) {
                iPPack.setPackQuantity(iPPack.getPackQuantity() + quantity);
            }else if(type.equals("remove")) {
                iPPack.setPackQuantity(iPPack.getPackQuantity() - quantity);
            }
            iPPack.getInventoryProduct().setUpdatedAt(LocalDateTime.now());
            return iPPackRepository.save(iPPack);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Pack not found"));
        return "Pack quantity updated successfully";
    }


    // get all category products of a particular seller
    public List<InventoryProductDto> getAllCategoryProductsOfASeller(String sellerInventoryId, String category) {

        log.info("[InventoryService:getAllCategoryProductsOfASeller] started for seller having seller inventory id: {} ",sellerInventoryId);

        List<InventoryProduct> inventoryProducts = inventoryRepository.findBySellerInventoryId(sellerInventoryId).get().getInventoryProducts()
                .stream()
                .filter(ip -> ip.getProduct().getSubcategory().getCategory().getCategoryName().equals(category)).collect(Collectors.toList());

        return inventoryProducts.stream().map(this::convertInventoryProductToInventoryProductDto).collect(Collectors.toList());
    }
    
    // get all subcategory products of a particular seller
    public List<InventoryProductDto> getAllSubcategoryProductsOfASeller(String sellerInventoryId, String subcategory) {

        log.info("[InventoryService:getAllSubcategoryProductsOfASeller] started for seller having seller inventory id: {} ",sellerInventoryId);
        List<InventoryProduct> inventoryProducts = inventoryRepository.findBySellerInventoryId(sellerInventoryId).get().getInventoryProducts()
                .stream()
                .filter(ip -> ip.getProduct().getSubcategory().getSubCategoryName().equals(subcategory)).collect(Collectors.toList());      

        return inventoryProducts.stream().map(this::convertInventoryProductToInventoryProductDto).collect(Collectors.toList());
    }


    // group by category and subcategory
    public Map<String, Map<String, List<InventoryProductDto>>> groupByCategoryAndSubcategory (String sellerInventoryId) {

        log.info("[InventoryService:groupByCategoryAndSubcategory] started for seller having seller inventory id: {} ",sellerInventoryId);
       List<InventoryProduct> inventoryProducts = inventoryRepository.findBySellerInventoryId(sellerInventoryId).get().getInventoryProducts();

      return  inventoryProducts.stream()
               .collect(Collectors
                       .groupingBy(inventoryProduct ->
                               inventoryProduct.getProduct()
                                       .getSubcategory().getCategory()
                                       .getCategoryName(),
                               Collectors.groupingBy(inventoryProduct ->
                                       inventoryProduct.getProduct()
                                               .getSubcategory().getSubCategoryName(),
                                       Collectors.mapping(this::convertInventoryProductToInventoryProductDto,Collectors.toList()))));

    
    }


        // update featured status of inventory product by inventory product id
            public InventoryProductDto updateFeaturedStatusByInventoryProductId(Long inventoryProductId) {

                log.info("[InventoryService:updateFeaturedStatusByInventoryProductId] started for inventory product having inventory product id: {} ",inventoryProductId);

                InventoryProduct inventoryProduct = inventoryProductRepository
                        .findById(inventoryProductId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Inventory product not found"));

                inventoryProduct.setFeatured(!inventoryProduct.isFeatured());
                inventoryProduct.setUpdatedAt(LocalDateTime.now());
                inventoryProductRepository.save(inventoryProduct);
                return convertInventoryProductToInventoryProductDto(inventoryProduct);

            }


       // apply discount to inventory products of a seller by seller inventory id and seller discount
       // it is a group discounts
        public List<InventoryProductDto> applyDiscountToInventoryProductsByCategoryWise(String sellerInventoryId, double sellerDiscount) {
            return inventoryRepository.findBySellerInventoryId(sellerInventoryId).get().getInventoryProducts().stream().map(inventoryProduct -> applyDiscountToInventoryProduct(inventoryProduct.getIpId(), sellerDiscount)).collect(Collectors.toList());
        }



        // apply discount to individual inventory product

    public InventoryProductDto applyDiscountToInventoryProduct(Long inventoryProductId, double discount) {

        InventoryProduct inventoryProduct = inventoryProductRepository.findById(inventoryProductId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Inventory product not found"));
        inventoryProduct.setSellerDiscount(discount);
        inventoryProduct.getProductAvailablePacks().forEach(pack ->{
            pack.packPriceAfterDiscount(discount);
            iPPackRepository.save(pack);
        });
        inventoryProduct.setUpdatedAt(LocalDateTime.now());

        inventoryProductRepository.save(inventoryProduct);
        return convertInventoryProductToInventoryProductDto(inventoryProduct);
    }

    // get all featured inventroy products of a seller
    public List<InventoryProductDto> getAllFeaturedInventroyProductsOfASeller(String sellerInventoryId) {

        log.info("[InventoryService:getAllFeaturedInventroyProductsOfASeller] started for seller having seller inventory id: {} ",sellerInventoryId);
        List<InventoryProduct> inventoryProducts = inventoryRepository.findBySellerInventoryId(sellerInventoryId).get().getInventoryProducts()
                .stream()
                .filter(InventoryProduct::isFeatured).toList();

        return inventoryProducts.stream().map(this::convertInventoryProductToInventoryProductDto).collect(Collectors.toList());
    }

    // check seller discount or general discount more than product price
    public boolean checkDiscount(double sellerDiscount, double productPrice) {

        if(sellerDiscount > productPrice) {
            return true;
        }
        return false;
    }


    // convert inventory product to inventory product dto
    public InventoryProductDto convertInventoryProductToInventoryProductDto(InventoryProduct inventoryProduct) {
        IProductDto iProductDto = new IProductDto();
        iProductDto.setProductId(inventoryProduct.getProduct().getProductId());
        iProductDto.setProductName(inventoryProduct.getProduct().getProductName());


       ISubCategoryDto iSubCategoryDto = new ISubCategoryDto();

        ICategoryDto iCategoryDto = new ICategoryDto();
        iCategoryDto.setCategoryId(inventoryProduct.getProduct().getSubcategory().getCategory().getCategoryId());
        iCategoryDto.setCategoryName(inventoryProduct.getProduct().getSubcategory().getCategory().getCategoryName());
        iCategoryDto.setCategoryImage(convertImageToImageDto(inventoryProduct.getProduct().getSubcategory().getCategory().getCategoryImage()));


       iSubCategoryDto.setSubcategoryId(inventoryProduct.getProduct().getSubcategory().getSubCategoryId());
       iSubCategoryDto.setSubcategoryName(inventoryProduct.getProduct().getSubcategory().getSubCategoryName());
       iSubCategoryDto.setSubcategoryImage(convertImageToImageDto(inventoryProduct.getProduct().getSubcategory().getSubCategoryImage()));
       iSubCategoryDto.setCategory(iCategoryDto);

       iProductDto.setSubcategory(iSubCategoryDto);
       iProductDto.setPrice(inventoryProduct.getProduct().getPrice());
       iProductDto.setGeneralDiscount(inventoryProduct.getProduct().getGeneralDiscount());
       iProductDto.setAttributes(inventoryProduct.getProduct().getDynamicAttributes());
       iProductDto.setSku(inventoryProduct.getProduct().getSku());

       return InventoryProductDto.builder()
               .ipId(inventoryProduct.getIpId())
               .product(iProductDto)
               .sellerDiscount(inventoryProduct.getSellerDiscount())
               .soldQuantity(inventoryProduct.getSoldQuantity())

               .featured(inventoryProduct.isFeatured())
               .productAvailablePacks(inventoryProduct.getProductAvailablePacks().stream().map(this::convertIPPackToIppackDto).collect(Collectors.toList()))

               .createdAt(inventoryProduct.getCreatedAt())
               .updatedAt(inventoryProduct.getUpdatedAt())

               .build();
}

// convert ippack to ippack dto
    private IPPackDto convertIPPackToIppackDto(IPPack iPPack) {

        return IPPackDto.builder()
                .packId(iPPack.getPackId())
                .pack(convertPackToPackDto(iPPack.getPack()))
                .finalPackPrice(iPPack.getFinalPackPrice())
                .packQuantity(iPPack.getPackQuantity())
                .build();
    }

    private PackDto convertPackToPackDto(Pack pack) {
        return PackDto.builder()
                .packName(pack.getPackName())
                .packId(pack.getPackId())
                .packPrice(pack.getPackPrice())
                .images(pack.getImages().stream().map(this::convertImageToImageDto).collect(Collectors.toList()))
                .build();
    }

    public ImageDto convertImageToImageDto(Image image) {

        return ImageDto.builder()
                .imageId(image.getImageId())
                .imageUrl(image.getImageUrl())
                .build();
    }


    // convert inventory to inventory dto
    public InventoryDto convertInventoryToInventoryDto(Inventory inventory) {
        return InventoryDto.builder()
                .inventoryId(inventory.getInventoryId())
                .sellerInventoryId(inventory.getSellerInventoryId())
                .inventoryProducts(

                        Optional.ofNullable(
                        inventory.getInventoryProducts())
                                .orElse(Collections.emptyList())
                                .stream()
                                .map(this::convertInventoryProductToInventoryProductDto).
                                collect(Collectors.toList()))
                .build();
    }




// update seller inventory product quantity of a particular pack for each new order
    // here only pack that was ordered is updated with new quantity

    public InventoryProductDto updateQuantityOfInventoryProductAfterOrderPlaced(Long inventoryProductId,Long packId, Long orderId, int quantity) {
       // Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Order not found"));
            Order order = new Order();
            if(order.getOrderStatus().equals(OrderStatus.INITIATED)) {
                InventoryProduct inventoryProduct = inventoryProductRepository.findById(inventoryProductId).orElse(null);

                inventoryProduct.getProductAvailablePacks().stream()
                        .filter(pack -> pack.getPackId() == packId).findFirst().ifPresent(p -> {
                            p.setPackQuantity(p.getPackQuantity() - quantity);
                            iPPackRepository.save(p);
                            inventoryProductRepository.save(inventoryProduct);
                        });
                return convertInventoryProductToInventoryProductDto(inventoryProduct);

            }
        throw new ItemNotFoundException("Order not placed yet or some other problem during placing order");
    }


    // update seller inventory product quantity for order cancelled
    // here only pack that was ordered is updated with new quantity but not total sold items of the base product

    public InventoryProductDto updateQuantityOfInventoryProductAfterOrderCancelled(Long inventoryProductId,Long packId, Long orderId, int quantity) {
        // Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Order not found"));
        Order order = new Order();
        if(order.getOrderStatus().equals(OrderStatus.CANCELLED)) {
            InventoryProduct inventoryProduct = inventoryProductRepository.findById(inventoryProductId).orElse(null);

            inventoryProduct.getProductAvailablePacks().stream()
                    .filter(pack -> pack.getPackId() == packId).findFirst().ifPresent(p -> {
                        p.setPackQuantity(p.getPackQuantity() + quantity);
                        iPPackRepository.save(p);
                        inventoryProductRepository.save(inventoryProduct);
                    });
            return convertInventoryProductToInventoryProductDto(inventoryProduct);

        }

        throw new ItemNotFoundException("Order not cancelled yet or some other problem during cancellation");

    }



    // update seller inventory product quantity for order delivered
    // here base product total sold quantity is updated and when order placed total quantity of that particular pack is updated

    public InventoryProductDto updateQuantityOfInventoryProductAfterOrderDelivered(Long inventoryProductId,Long packId, Long orderId, int quantity) {
        // Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Order not found"));
        Order order = new Order();
        if (order.getOrderStatus().equals(OrderStatus.DELIVERED)) {
            InventoryProduct inventoryProduct = inventoryProductRepository.findById(inventoryProductId).orElse(null);

            inventoryProduct.setSoldQuantity(inventoryProduct.getSoldQuantity() + quantity);
            inventoryProductRepository.save(inventoryProduct);
            return convertInventoryProductToInventoryProductDto(inventoryProduct);
        }
        throw new ItemNotFoundException("Order not delivered yet or some other problem during delivery");
    }



    // for customer ui
    
    

    // get single product by inventory product id( prod id)
    
    public ProductResponse getSingleProductByInventoryProductId(Long inventoryProductId) {
        InventoryProductDto inventoryProductDto = getInventoryProductByInventoryProductId(inventoryProductId);
        return convertInventoryProductDtoToProductResponse(inventoryProductDto);
    }

    // get products of a sub category
    
    public List<ProductResponse> getProductsBySubCategory(String sellerInventoryId, String subCategory) {
        List<InventoryProductDto> inventoryProductDtos = getAllSubcategoryProductsOfASeller(sellerInventoryId,subCategory);
        log.info("[InventoryServiceImpl:getProductsBySubCategory] inventoryProductDtos: {} , {},{} ",inventoryProductDtos,sellerInventoryId,subCategory);
        return inventoryProductDtos.stream().map(this::convertInventoryProductDtoToProductResponse).collect(Collectors.toList());
    }
    
    // get recently added sub category products

    public List<ProductResponse> getRecentlyAddedSubCategoryProductsForCustomer(String sellerInventoryId, String subCategory) {
        List<InventoryProductDto> inventoryProductDtos = getRecentlyModifiedProductsBySellerInLastHours(sellerInventoryId,48).stream().filter(ip -> ip.getProduct().getSubcategory().getSubcategoryName().equals(subCategory)).collect(Collectors.toList());
        return inventoryProductDtos.stream().map(this::convertInventoryProductDtoToProductResponse).collect(Collectors.toList());
    }
    
    // get all featured products

    public List<ProductResponse> getAllFeaturedInventoryProductsOfASellerForCustomer(String sellerInventoryId) {
        List<InventoryProductDto> inventoryProductDtos = getAllFeaturedInventroyProductsOfASeller(sellerInventoryId);

        return inventoryProductDtos.stream().map(this::convertInventoryProductDtoToProductResponse).toList();

    }

    // get all category products for customer of a particular seller

    public List<ProductResponse> getAllCategoryProductsOfASellerForCustomer(String sellerInventoryId, String category) {

        List<InventoryProductDto> inventoryProductDtos = getAllCategoryProductsOfASeller(sellerInventoryId,category);
        return inventoryProductDtos.stream().map(this::convertInventoryProductDtoToProductResponse).toList();

    }

    // get all products of a seller for customer

    public List<ProductResponse> getAllProductsOfASellerForCustomer(String sellerInventoryId) {

        List<InventoryProductDto> inventoryProductDtos = inventoryRepository.findBySellerInventoryId(sellerInventoryId).get().getInventoryProducts().stream().map(this::convertInventoryProductToInventoryProductDto).toList();
        return inventoryProductDtos.stream().map(this::convertInventoryProductDtoToProductResponse).toList();
    }
    
    

    // convert inventory product dto to product response
    public ProductResponse convertInventoryProductDtoToProductResponse(InventoryProductDto inventoryProductDto) {
        return ProductResponse.builder()
                .prodId(inventoryProductDto.getIpId())
                .product(convertIProductDtoToIProductRDto(inventoryProductDto.getProduct()))
                .sellerDiscount(inventoryProductDto.getSellerDiscount())
                .featured(inventoryProductDto.isFeatured())
                .productAvailablePacks(inventoryProductDto.getProductAvailablePacks())
                .createdAt(inventoryProductDto.getCreatedAt())
                .updatedAt(inventoryProductDto.getUpdatedAt())
                .build();

    }


    // convert IProductDto to IProductRDto

    public IProductRDto convertIProductDtoToIProductRDto(IProductDto iProductDto) {
        return IProductRDto.builder()

                .productName(iProductDto.getProductName())
                .subcategory(iProductDto.getSubcategory())
                .price(iProductDto.getPrice())
                .generalDiscount(iProductDto.getGeneralDiscount())
                .attributes(iProductDto.getAttributes())
                .sku(iProductDto.getSku())
                .build();
    }

    // get all inventories

    public List<Inventory> getAllInventroies(){
        return inventoryRepository.findAll();
    }

    public InventoryProduct getInventoryProductById(Long inventoryProductId){
        return inventoryProductRepository.findById(inventoryProductId).orElseThrow(() -> new ItemNotFoundException("Product not found"));
    }

}