package com.zodo.kart.controller.order;

import com.phonepe.sdk.pg.common.http.PhonePeResponse;
import com.phonepe.sdk.pg.payments.v1.models.response.PgPayResponse;
import com.zodo.kart.dto.order.OrderRequestDto;
import com.zodo.kart.dto.order.OrderResponseDto;
import com.zodo.kart.entity.order.Order;
import com.zodo.kart.enums.OrderEvent;
import com.zodo.kart.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Author : Bhanu prasad
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/order")
public class OrderController {

    private final OrderService orderService;


    // initiate order
    @PostMapping("/initiate")
    public PhonePeResponse<PgPayResponse> initiateOrder(@RequestBody OrderRequestDto orderRequestDto) {
        return orderService.initiateOrder(orderRequestDto);
    }

    // get all orders

    @GetMapping("/orders")
    public List<Order> getAllOrders(){
        return orderService.getAllOrders();
    }


    @GetMapping("/order/{merchantTransactionId}")
    public Order getOrderByMerchantTransactionId(@PathVariable String merchantTransactionId){
        return orderService.getOrderByMerchantTransactionId(merchantTransactionId);
    }


    @GetMapping("/user/{customerId}")
    public List<OrderResponseDto> getAllOrdersByCustomerId(@PathVariable Long customerId){
        return orderService.getAllOrdersByCustomerId(customerId);
    }


    @PutMapping("/{orderId}/status")
    public ResponseEntity<String> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String orderEvent) {

        // Convert string to OrderEvent enum
        OrderEvent event = OrderEvent.valueOf(orderEvent.toUpperCase());
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, event));
    }

    @GetMapping("/seller/active-orders/{sellerId}")
    public ResponseEntity<List<OrderResponseDto>> getAllActiveSellerOrdersOfCustomers(String sellerId){
        return ResponseEntity.ok(orderService.getAllActiveSellerOrdersOfCustomers(sellerId));
    }

    @GetMapping("/seller/orders/{sellerId}")
    public ResponseEntity<List<OrderResponseDto>> getAllSellerOrdersOfCustomers(String sellerId){
        return ResponseEntity.ok(orderService.getAllSellerOrdersOfCustomers(sellerId));
    }


}
