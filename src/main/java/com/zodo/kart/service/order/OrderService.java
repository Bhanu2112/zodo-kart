package com.zodo.kart.service.order;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.sdk.pg.common.http.PhonePeResponse;
import com.phonepe.sdk.pg.payments.v1.models.response.PgPayResponse;
import com.phonepe.sdk.pg.payments.v1.models.response.PgTransactionStatusResponse;
import com.zodo.kart.dto.inventory.InventoryProductDto;
import com.zodo.kart.dto.inventory.ProductResponse;
import com.zodo.kart.dto.order.OrderItemResponseDto;
import com.zodo.kart.dto.order.OrderRequestDto;
import com.zodo.kart.dto.order.OrderItemRequestDto;
import com.zodo.kart.dto.order.OrderResponseDto;
import com.zodo.kart.entity.inventory.InventoryProduct;
import com.zodo.kart.entity.order.Order;
import com.zodo.kart.entity.order.OrderItems;
import com.zodo.kart.entity.order.Payment;
import com.zodo.kart.enums.OrderEvent;
import com.zodo.kart.enums.OrderStatus;
import com.zodo.kart.enums.PaymentMethods;
import com.zodo.kart.enums.PaymentStatus;
import com.zodo.kart.exceptions.ItemNotFoundException;
import com.zodo.kart.repository.order.OrderRepository;
import com.zodo.kart.repository.order.PaymentRepository;
import com.zodo.kart.repository.users.AddressRepository;
import com.zodo.kart.service.inventory.InventoryServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Author : Bhanu prasad
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final AddressRepository addressRepository;

    private final OrderRepository orderRepository;

    private final PaymentRepository paymentRepository;

    private final PhonePeService phonePeService;

    private final InventoryServiceImpl inventoryService;

    private final StateMachineFactory<OrderStatus, OrderEvent> stateMachineFactory;

    @Value("${phonepe.pg.redirectUrl}")
    private String redirectUrl; // Redirect URL
    @Value("${phonepe.pg.callbackUrl}")
    private String callbackUrl;

    // update order status

    public String updateOrderStatus(Long orderId, OrderEvent orderEvent) {

        log.info("[updateOrderStatus] orderId: {}, orderEvent: {}", orderId, orderEvent);

        Order order = orderRepository.findById(orderId)
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

        if(previousState != afterState){
            return "Order status updated successfully to: " + afterState;
        }else {
            return "Order status not updated";
        }

    }


    // initiate order

    public PhonePeResponse<PgPayResponse> initiateOrder(OrderRequestDto orderRequestDto){

        // create order object

        Order order = new Order();

        order.setUserId(orderRequestDto.getUserId());
        order.setSellerId(orderRequestDto.getSellerId());
        order.setTotalPrice(orderRequestDto.getTotalPrice());

        // get shipping address

      //  Address address = addressRepository.findById(orderDto.getShippingAddressId()).get();

       // order.setShippingAddress(address);

        order.setOrderStatus(OrderStatus.INITIATED);
        order.setOrderDate(LocalDateTime.now());
        order.setPaymentStatus(PaymentStatus.PENDING);

        // get all products from order dto

        List<OrderItems> orderItems = orderRequestDto.getOrderItems().stream().map(orderItemRequestDto -> convertToOrderItems(orderItemRequestDto, order)).toList();

        order.setOrderItems(orderItems);

        // create payment object

        Payment payment = new Payment();

        String merchantTransactionId = UUID.randomUUID().toString().substring(0,34);
        payment.setMerchantTransactionId(merchantTransactionId);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setPaymentAmount(orderRequestDto.getTotalPrice());
        payment.setOrder(order);


        // setting payment object to order

        order.setPayment(payment);

        // save order

        orderRepository.save(order);


        // initiate payment page

        System.out.println(callbackUrl);
        System.out.println(redirectUrl);

        PhonePeResponse<PgPayResponse> paymentResponse = phonePeService.initiatePayment(
                orderRequestDto.getTotalPrice(),
                redirectUrl,
                callbackUrl,
                merchantTransactionId,
                order.getOrderId(),
                order.getUserId()
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
        Order order = getOrderByMerchantTransactionId(merchantTransactionId);


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

    public Order getOrderByMerchantTransactionId(String merchantTransactionId){
        return orderRepository.findByPayment_MerchantTransactionId(merchantTransactionId).get();
    }


    // get all orders

    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }


    private OrderItems convertToOrderItems(OrderItemRequestDto orderItemRequestDto, Order order) {
        OrderItems orderItem = new OrderItems();

        orderItem.setPackId(orderItemRequestDto.getPackId());
        orderItem.setProductId(orderItemRequestDto.getProductId());
        orderItem.setQuantity(orderItemRequestDto.getQuantity());
        orderItem.setPrice(orderItemRequestDto.getPrice());
        orderItem.setOrder(order);
        return orderItem;
    }



    // get all orders of the customer


    public List<OrderResponseDto> getAllOrdersByCustomerId(Long customerId){
     List<Order> orders =  orderRepository.findAllOrdersByUserId(customerId);
     return orders.stream().map(this::convertOrderToOrderResponseDto).toList();
    }

    private OrderResponseDto convertOrderToOrderResponseDto(Order order) {
        return OrderResponseDto.builder()
                .orderId(order.getOrderId())
                .orderStatus(order.getOrderStatus())
                .totalPrice(order.getTotalPrice())
                .paymentStatus(order.getPayment().getPaymentStatus())
                .paymentMethod(order.getPayment().getPaymentMethod())
                .orderDate(order.getOrderDate())
                .userId(order.getUserId())
                .shippingDetails(order.getShippingDetails())
                .sellerId(order.getSellerId())
                .orderItems(order.getOrderItems().stream().map(this::convertOrderItemToOrderItemResponseDto).toList())
                .build();
    }


    public ProductResponse convertInventoryProductToProductResponse(InventoryProduct inventoryProduct) {
        InventoryProductDto inventoryProductDto =  inventoryService.convertInventoryProductToInventoryProductDto(inventoryProduct);
        ProductResponse productResponse = inventoryService.convertInventoryProductDtoToProductResponse(inventoryProductDto);
        return productResponse;
    }

    // get payment by id

    public Payment getPaymentById(Long paymentId){
        return paymentRepository.findById(paymentId).get();
    }

    // get all payments

    public List<Payment> getAllPayments(){
        return paymentRepository.findAll();
    }




    // Convert to Order item to OrderItemResponseDto

    public OrderItemResponseDto convertOrderItemToOrderItemResponseDto(OrderItems orderItem){
        return OrderItemResponseDto.builder()
                .orderItemId(orderItem.getOrderItemId())
                .price(orderItem.getPrice())
                .packId(orderItem.getPackId())
                .quantity(orderItem.getQuantity())
                .product(convertInventoryProductToProductResponse(inventoryService.getInventoryProductById(orderItem.getProductId())))
                .build();

    }


    // get all seller orders, order status should not be initiated.

    public List<OrderResponseDto> getAllSellerOrdersOfCustomers(String sellerId){
        List<Order> orders = orderRepository
                .findAllOrdersBySellerId(sellerId)
                .stream()
                .filter(order -> order.getOrderStatus() != OrderStatus.INITIATED)
                .toList();
        return orders.stream().map(this::convertOrderToOrderResponseDto).toList();
    }


    // get all active orders of seller

    public List<OrderResponseDto> getAllActiveSellerOrdersOfCustomers(String sellerId){
        List<OrderStatus> activeStatuses = Arrays.asList(
                OrderStatus.PENDING,
                OrderStatus.ACCEPTED,
                OrderStatus.PROCESSING,
                OrderStatus.SHIPPED
        );

        return orderRepository.findBySellerIdAndOrderStatusIn(sellerId,activeStatuses)
                .stream()
                .map(this::convertOrderToOrderResponseDto).toList();

    }






}
