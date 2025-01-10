package com.zodo.kart.controller.order;

import com.zodo.kart.entity.order.DeliveryAssignment;
import com.zodo.kart.enums.OrderEvent;
import com.zodo.kart.service.order.ShippingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Author : Bhanu prasad
 */

@RestController
@RequestMapping("/public/shipping")
@RequiredArgsConstructor
public class ShippingController {

    private final ShippingService shippingService;

    @PostMapping("/assign")
    public ResponseEntity<String> assignOrderToDeliveryPerson(
            @RequestParam Long orderId,
            @RequestParam Long deliveryPersonId) {

        String responseMessage = shippingService.assignOrderToDeliveryPerson(orderId, deliveryPersonId);
        return ResponseEntity.ok(responseMessage);
    }

    // Endpoint to get all active delivery assignments for a delivery person
    @GetMapping("/active-orders/{deliveryPersonId}")
    public ResponseEntity<List<DeliveryAssignment>> getActiveOrdersOfDeliveryPerson(
            @PathVariable Long deliveryPersonId) {

        List<DeliveryAssignment> activeOrders = shippingService.getActiveOrdersOfDeliveryPerson(deliveryPersonId);
        return ResponseEntity.ok(activeOrders);
    }

    // Endpoint to get all completed delivery assignments for a delivery person
    @GetMapping("/order-completed/{deliveryPersonId}")
    public ResponseEntity<List<DeliveryAssignment>> getCompletedOrdersOfDeliveryPerson(
            @PathVariable Long deliveryPersonId) {

        List<DeliveryAssignment> completedOrders = shippingService.getCompletedOrdersOfDeliveryPerson(deliveryPersonId);
        return ResponseEntity.ok(completedOrders);
    }

    // Endpoint to update the order status
    @PutMapping("/update/order-status")
    public ResponseEntity<String> updateOrderStatus(
            @RequestParam Long orderId,
            @RequestParam Long deliveryAssignmentId,
            @RequestParam OrderEvent event) {

        String responseMessage = shippingService.updateOrderStatus(orderId, deliveryAssignmentId, event);
        return ResponseEntity.ok(responseMessage);
    }
    
}
