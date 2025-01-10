package com.zodo.kart.controller.order;

import com.phonepe.sdk.pg.common.http.PhonePeResponse;
import com.phonepe.sdk.pg.payments.v1.models.response.PgPayResponse;
import com.zodo.kart.dto.order.OrderRequestDto;
import com.zodo.kart.dto.order.sellerorders.SOrderRequestDto;
import com.zodo.kart.dto.order.sellerorders.SOrderResponseDto;
import com.zodo.kart.entity.inventory.InventoryProduct;
import com.zodo.kart.entity.order.OperatorOrder;
import com.zodo.kart.entity.order.Order;
import com.zodo.kart.enums.OrderEvent;
import com.zodo.kart.service.sellerorder.OperatorOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Author : Bhanu prasad
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/operator/orders")
public class OperatorOrderController {


    private final OperatorOrderService operatorOrderService;


    @GetMapping("/all")
    public List<OperatorOrder> getAllOperatorsOrders(){
        return operatorOrderService.getAllOrders();
    }

    // initiate order
    @PostMapping("/initiate")
    public PhonePeResponse<PgPayResponse> initiateOrder(@RequestBody SOrderRequestDto orderRequestDto) {
        return operatorOrderService.initiateOrder(orderRequestDto);
    }

    @GetMapping("/order/{merchantTransactionId}")
    public OperatorOrder getOrderByMerchantTransactionId(@PathVariable String merchantTransactionId){
        return operatorOrderService.getOrderByMerchantTransactionId(merchantTransactionId);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<String> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String orderEvent) {

        // Convert string to OrderEvent enum
        OrderEvent event = OrderEvent.valueOf(orderEvent.toUpperCase());
        return ResponseEntity.ok(operatorOrderService.updateOrderStatus(orderId, event));
    }

    // get all active orders of operator

    @GetMapping("/active-orders/{operatorId}")
    public ResponseEntity<List<SOrderResponseDto>> getAllActiveOrdersOfOperator(@PathVariable String operatorId){
        return ResponseEntity.ok(operatorOrderService.getAllActiveOrdersOfOperator(operatorId));
    }

    // get all orders of operator

    @GetMapping("/orders/{operatorId}")
    public ResponseEntity<List<SOrderResponseDto>> getAllOrdersByOperatorId(@PathVariable String operatorId){
        return ResponseEntity.ok(operatorOrderService.getAllOrdersByOperatorId(operatorId));
    }


    // get all active seller orders of operator
    @GetMapping("/seller/active-orders/{sellerId}")
    public ResponseEntity<List<SOrderResponseDto>> getAllActiveSellerOrdersOfOperator(@PathVariable String sellerId){
        return ResponseEntity.ok(operatorOrderService.getAllActiveSellerOrdersOfOperator(sellerId));
    }


        // get inventory product by product id
//
//    @GetMapping("/inventory/product/{productId}")
//    public InventoryProduct getInventoryProduct(@PathVariable Long productId){
//        return operatorOrderService.getInventoryProductByProductId(productId);
//    }

}
