package com.zodo.kart.service.sellerorder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.sdk.pg.common.http.PhonePeResponse;
import com.phonepe.sdk.pg.payments.v1.models.response.PgPayResponse;
import com.phonepe.sdk.pg.payments.v1.models.response.PgTransactionStatusResponse;
import com.zodo.kart.dto.inventory.InventoryProductDto;
import com.zodo.kart.dto.inventory.ProductResponse;
import com.zodo.kart.dto.order.OrderItemRequestDto;
import com.zodo.kart.dto.order.OrderItemResponseDto;
import com.zodo.kart.dto.order.OrderResponseDto;
import com.zodo.kart.dto.order.sellerorders.SOrderItemRequestDto;
import com.zodo.kart.dto.order.sellerorders.SOrderItemResponseDto;
import com.zodo.kart.dto.order.sellerorders.SOrderRequestDto;
import com.zodo.kart.dto.order.sellerorders.SOrderResponseDto;
import com.zodo.kart.entity.inventory.IPPack;
import com.zodo.kart.entity.inventory.Inventory;
import com.zodo.kart.entity.inventory.InventoryProduct;
import com.zodo.kart.entity.order.OperatorOrder;
import com.zodo.kart.entity.order.Order;
import com.zodo.kart.entity.order.OrderItems;
import com.zodo.kart.entity.order.Payment;
import com.zodo.kart.entity.product.Product;
import com.zodo.kart.entity.users.Address;
import com.zodo.kart.enums.OrderEvent;
import com.zodo.kart.enums.OrderStatus;
import com.zodo.kart.enums.PaymentMethods;
import com.zodo.kart.enums.PaymentStatus;
import com.zodo.kart.exceptions.ItemNotFoundException;
import com.zodo.kart.repository.inventory.IPPackRepository;
import com.zodo.kart.repository.inventory.InventoryProductRepository;
import com.zodo.kart.repository.inventory.InventoryRepository;
import com.zodo.kart.repository.order.OperatorOrderRepository;
import com.zodo.kart.repository.order.PaymentRepository;
import com.zodo.kart.repository.product.ProductRepository;
import com.zodo.kart.repository.users.AddressRepository;
import com.zodo.kart.service.inventory.InventoryServiceImpl;
import com.zodo.kart.service.order.PhonePeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.*;


/**
 * Author : Bhanu prasad
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class OperatorOrderService {



    private final OperatorOrderRepository orderRepository;
    private final AddressRepository addressRepository;

    private final PaymentRepository paymentRepository;
    private final PhonePeService phonePeService;
    private final InventoryServiceImpl inventoryService;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final InventoryProductRepository inventoryProductRepository;
    private final StateMachineFactory<OrderStatus, OrderEvent> stateMachineFactory;
    private final IPPackRepository packRepository;

    @Value("${phonepe.pg.operatorRedirectUrl}")
    private String redirectUrl; // Redirect URL
    @Value("${phonepe.pg.operatorCallbackUrl}")
    private String callbackUrl;

    // initiate order


    public PhonePeResponse<PgPayResponse> initiateOrder(SOrderRequestDto sOrderRequestDto){
        OperatorOrder order = new OperatorOrder();
        order.setOrderStatus(OrderStatus.INITIATED);
        order.setOperatorId(sOrderRequestDto.getOperatorId());
        order.setSellerId(sOrderRequestDto.getSellerId());
        order.setTotalPrice(sOrderRequestDto.getTotalPrice());

        // get shipping address

        // Address address = addressRepository.findById(sOrderRequestDto.getShopAddressId()).get();

        // order.setShippingAddress(address);

        order.setOrderDate(LocalDateTime.now());
        order.setPaymentStatus(PaymentStatus.PENDING);

        List<OrderItems> orderItems = sOrderRequestDto.getOrderItems().stream().map(item -> convertToOrderItems(item,order)).toList();


        order.setOrderItems(orderItems);

        Payment payment = new Payment();

        String merchantTransactionId = UUID.randomUUID().toString().substring(0,34);
        payment.setMerchantTransactionId(merchantTransactionId);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setPaymentAmount(sOrderRequestDto.getTotalPrice());
        payment.setOperatorOrder(order);

        order.setPayment(payment);

        orderRepository.save(order);

        // initiate payment page

        System.out.println(callbackUrl);
        System.out.println(redirectUrl);

        PhonePeResponse<PgPayResponse> paymentResponse = phonePeService.initiateOperatorPayment(
                sOrderRequestDto.getTotalPrice(),
                redirectUrl,
                callbackUrl,
                merchantTransactionId,
                order.getOrderId(),
                order.getOperatorId()
        );

        return paymentResponse;





    }



    // update order payment status and remaining details after call back response

    public String updateOrderPaymentStatusAfterPayment(String response) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseReq = null;
        try{
            responseReq = objectMapper.readTree(response);
        }catch (Exception e){
            throw new RuntimeException("Failed to decode and parse JSON");
        }
        System.out.println(responseReq.path("response").asText());
        JsonNode paymentResponse = phonePeService.decodeBase64Response(responseReq.path("response").asText());
        // code
        String code = paymentResponse.path("code").asText();
        String merchantTransactionId = paymentResponse.path("data").path("merchantTransactionId").asText();
        String providerReferenceId = paymentResponse.path("data").path("transactionId").asText();
        // paymentMethod
        String method = paymentResponse.path("data").path("paymentInstrument").path("type").asText();
        // state
        String state = paymentResponse.path("data").path("state").asText();
        System.out.println(merchantTransactionId + " " + providerReferenceId + " " + method + " " + state);
        // re verify status of the payment using phonepe check status
        PhonePeResponse<PgTransactionStatusResponse> statusResponse = phonePeService.checkPaymentStatus(merchantTransactionId);
        code = statusResponse.getCode();
        state = statusResponse.getData().getState();
        providerReferenceId = statusResponse.getData().getTransactionId();
        method = String.valueOf(statusResponse.getData().getPaymentInstrument().getType());
        OperatorOrder order = getOrderByMerchantTransactionId(merchantTransactionId);


        if(state.equals("COMPLETED") && code.equals("PAYMENT_SUCCESS")){
            order.setOrderStatus(OrderStatus.PENDING);
            order.setPaymentStatus(PaymentStatus.SUCCESS);
            order.setPaymentMethod(PaymentMethods.valueOf(method));
            // payment
            order.getPayment().setPaymentStatus(PaymentStatus.SUCCESS);
            order.getPayment().setPaymentMethod(PaymentMethods.valueOf(method));
            order.getPayment().setProviderReferenceId(providerReferenceId);
            orderRepository.save(order);
            return "Payment was successful, and the order status has been updated to PENDING.";
        }
        if(state.equals("FAILED") && code.equals("PAYMENT_ERROR")){
            order.setOrderStatus(OrderStatus.PAYMENT_ERROR);
            order.setPaymentStatus(PaymentStatus.FAILED);
            //  order.setPaymentMethod(PaymentMethods.valueOf(method));

            // payment

            order.getPayment().setPaymentStatus(PaymentStatus.FAILED);

            orderRepository.save(order);
            return "Payment failed, and the order status has been updated to PAYMENT_ERROR.";

        }

        return "Order payment status remains unchanged.";
    }

    // get order by merchantTransactionId

    public OperatorOrder getOrderByMerchantTransactionId(String merchantTransactionId){
        return orderRepository.findByPayment_MerchantTransactionId(merchantTransactionId).get();
    }


    // update order status

    public String updateOrderStatus(Long orderId, OrderEvent orderEvent) {

        log.info("[updateOrderStatus] orderId: {}, orderEvent: {}", orderId, orderEvent);

        OperatorOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ItemNotFoundException("Order not found"));

        // Get the state machine instance
        StateMachine<OrderStatus, OrderEvent> stateMachine = stateMachineFactory.getStateMachine(order.getOrderId().toString());
        // Set the state machine to the current state of the order
        stateMachine.getStateMachineAccessor().doWithAllRegions(accessor -> {
            accessor.resetStateMachineReactively(
                    new DefaultStateMachineContext<>(order.getOrderStatus(), null, null, null)
            ).block();
        });


        // Start the state machine
        stateMachine.startReactively().block();

        // Log current state and event
        log.info("Current State Before Event: {}", stateMachine.getState().getId());
        OrderStatus previousState = stateMachine.getState().getId();
        log.info("Sending Event: {}", orderEvent);

        // Send the event and wait for it to be processed
        stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(orderEvent).build())).blockLast();

        // Log the new state
        log.info("New State After Event: {}", stateMachine.getState().getId());
        OrderStatus afterState = stateMachine.getState().getId();


        // Update the order status based on the state machine's current state
        order.setOrderStatus(stateMachine.getState().getId());
        orderRepository.save(order);

        if(afterState == OrderStatus.DELIVERED){
            addPurchasedProductsToInventory(order);
        }

        if(previousState != afterState){
            return "Order status updated successfully to: " + afterState;
        }else {
            return "Order status not updated";
        }

    }


    // get all orders

    public List<OperatorOrder> getAllOrders(){
        return orderRepository.findAll();
    }

    // get all active orders of operator

    public List<SOrderResponseDto> getAllActiveOrdersOfOperator(String operatorId){
        List<OrderStatus> activeStatuses = Arrays.asList(
                OrderStatus.PENDING,
                OrderStatus.ACCEPTED,
                OrderStatus.PROCESSING,
                OrderStatus.SHIPPED
        );

        return orderRepository.findBySellerIdAndOrderStatusIn(operatorId
                        ,activeStatuses)
                .stream()
                .map(this::convertOrderToSOrderResponseDto).toList();

    }





    // get all orders of operator

    public List<SOrderResponseDto> getAllOrdersByOperatorId(String operatorId){
        List<OperatorOrder> orders =  orderRepository.findAllOrdersByOperatorId(operatorId);
        return orders.stream().map(this::convertOrderToSOrderResponseDto).toList();
    }

    // get all active admin orders of operators

    public List<SOrderResponseDto> getAllActiveSellerOrdersOfOperator(String sellerId){
        List<OrderStatus> activeStatuses = Arrays.asList(
                OrderStatus.PENDING,
                OrderStatus.ACCEPTED,
                OrderStatus.PROCESSING,
                OrderStatus.SHIPPED
        );

        return orderRepository.findBySellerIdAndOrderStatusIn(sellerId,activeStatuses)
                .stream()
                .map(this::convertOrderToSOrderResponseDto).toList();

    }

    // get all seller orders, order status should not be initiated.

    public List<SOrderResponseDto> getAllActiveSellerOrdersOfOperators(String sellerId){
        List<OperatorOrder> orders = orderRepository
                .findAllOrdersBySellerId(sellerId)
                .stream()
                .filter(order -> order.getOrderStatus() != OrderStatus.INITIATED)
                .toList();
        return orders.stream().map(this::convertOrderToSOrderResponseDto).toList();
    }




    // get all admin orders of operators


    // add purchased products to operator inventory after successfully purchased

    public String addPurchasedProductsToInventory(OperatorOrder order){

        // Inventory

        try {
            Inventory inventory = inventoryService.getInventoryBySellerInventoryId(order.getSellerId());

            if (inventory == null) {
                inventory = new Inventory();
                inventory.setSellerInventoryId(order.getSellerId());
            }

            //List<InventoryProduct> inventoryProducts = new ArrayList<>();

            List<OrderItems> orderItems = order.getOrderItems();

            for (OrderItems orderItem : orderItems) {
                Optional<InventoryProduct> inventoryProduct1 = inventory.getInventoryProducts().stream().filter(ip ->
                                ip.getProduct().getProductId() == orderItem.getProductId())
                        .findFirst();

                InventoryProduct iproduct = new InventoryProduct();
                if (inventoryProduct1.isPresent()) {

                    iproduct = inventoryProduct1.get();

                    Product product = productRepository.findById(orderItem.getProductId()).get();
                    iproduct.setProduct(product);
                    iproduct.setInventory(inventory);
                    iproduct.setCreatedAt(LocalDateTime.now());
                    iproduct.setUpdatedAt(LocalDateTime.now());


                    // available packs
                    IPPack pack = new IPPack();
                    pack.setInventoryProduct(iproduct);
                    pack.setPackQuantity(orderItem.getQuantity());
                    pack.packPriceAfterDiscount(0);
                    packRepository.save(pack);
                    iproduct.getProductAvailablePacks().add(pack);
                    inventoryProductRepository.save(iproduct);
                    inventoryRepository.save(inventory);

                } else {
                    // product setup for new inventory product
                    Product product = productRepository.findById(orderItem.getProductId()).get();

                    iproduct.setProduct(product);
                    iproduct.setInventory(inventory);
                    iproduct.setUpdatedAt(LocalDateTime.now());

                    // available packs

                    IPPack pack = iproduct.getProductAvailablePacks().stream().filter(p -> p.getPackId() == orderItem.getPackId()).findFirst().orElseGet(IPPack::new);
                    pack.setInventoryProduct(iproduct);

                    pack.setPackQuantity(pack.getPackQuantity() + orderItem.getQuantity());
                    pack.packPriceAfterDiscount(0);
                    packRepository.save(pack);
                    inventoryProductRepository.save(iproduct);
                    inventoryRepository.save(inventory);


                }

            }
        }catch (Exception e){
            throw new ItemNotFoundException("Unable to process");
        }

        return "Products updated in inventory";

    }

    // get inventory product by product id

    public InventoryProduct getInventoryProductByProductId(Long productId, String sellerId){
        return inventoryProductRepository.findByProduct_ProductId(productId).get();
    }





    private OrderItems convertToOrderItems(SOrderItemRequestDto orderItemRequestDto, OperatorOrder order) {
        OrderItems orderItem = new OrderItems();

        orderItem.setPackId(orderItemRequestDto.getPackId());
        orderItem.setProductId(orderItemRequestDto.getProductId());
        orderItem.setQuantity(orderItemRequestDto.getQuantity());
        orderItem.setPrice(orderItemRequestDto.getPrice());
        orderItem.setOperatorOrder(order);
        return orderItem;
    }

    // Convert to Order item to OrderItemResponseDto

    public SOrderItemResponseDto convertOrderItemToOrderItemResponseDto(OrderItems orderItem){
        return SOrderItemResponseDto.builder()
                .orderItemId(orderItem.getOrderItemId())
                .price(orderItem.getPrice())
                .packId(orderItem.getPackId())
                .quantity(orderItem.getQuantity())
                .product(convertInventoryProductToProductResponse(inventoryService.getInventoryProductById(orderItem.getProductId())))
                .build();

    }

    public ProductResponse convertInventoryProductToProductResponse(InventoryProduct inventoryProduct) {
        InventoryProductDto inventoryProductDto =  inventoryService.convertInventoryProductToInventoryProductDto(inventoryProduct);
        ProductResponse productResponse = inventoryService.convertInventoryProductDtoToProductResponse(inventoryProductDto);
        return productResponse;
    }

    private SOrderResponseDto convertOrderToSOrderResponseDto(OperatorOrder order) {
        return SOrderResponseDto.builder()
                .orderId(order.getOrderId())
                .orderStatus(order.getOrderStatus())
                .totalPrice(order.getTotalPrice())
                .paymentStatus(order.getPayment().getPaymentStatus())
                .paymentMethod(order.getPayment().getPaymentMethod())
                .orderDate(order.getOrderDate())
                .operatorId(order.getOperatorId())
                .shippingDetails(order.getShippingDetails())
                .sellerId(order.getSellerId())
                .orderItems(order.getOrderItems().stream().map(this::convertOrderItemToOrderItemResponseDto).toList())
                .build();
    }







}
