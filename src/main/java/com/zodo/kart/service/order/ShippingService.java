package com.zodo.kart.service.order;

import com.zodo.kart.entity.order.DeliveryAssignment;
import com.zodo.kart.entity.order.Order;
import com.zodo.kart.entity.users.DeliveryPerson;
import com.zodo.kart.enums.OrderEvent;
import com.zodo.kart.enums.OrderStatus;
import com.zodo.kart.exceptions.ItemNotFoundException;
import com.zodo.kart.repository.order.DeliveryAssignmentRepository;
import com.zodo.kart.repository.order.OrderRepository;
import com.zodo.kart.repository.users.operator.DeliveryPersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Author : Bhanu prasad
 */

@Service
@RequiredArgsConstructor
public class ShippingService {

    private final OrderService orderService;
    private final DeliveryAssignmentRepository deliveryAssignmentRepository;
    private final OrderRepository orderRepository;
    private final DeliveryPersonRepository deliveryPersonRepository;



    // assign order to delivery person

    public String assignOrderToDeliveryPerson(Long orderId, Long deliveryPersonId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ItemNotFoundException("Order not found"));

        DeliveryPerson deliveryPerson = deliveryPersonRepository.findById(deliveryPersonId)
                .orElseThrow(() -> new ItemNotFoundException("Delivery Person not found"));

        DeliveryAssignment deliveryAssignment = new DeliveryAssignment();
        deliveryAssignment.setOrder(order);
        deliveryAssignment.setDeliveryPerson(deliveryPerson);
        deliveryAssignment.setDeliveryStatus(OrderStatus.SHIPPED);
        deliveryAssignment.setAssignedAt(LocalDateTime.now());

        deliveryAssignmentRepository.save(deliveryAssignment);


        return "Order assigned to delivery person "+deliveryPerson.getPersonalDetails().getFirstName()+" successfully";
    }


    // get all active delivery assignments of delivery person

    public List<DeliveryAssignment> getActiveOrdersOfDeliveryPerson(Long deliveryPersonId) {
        List<OrderStatus> activeStatuses = Arrays
                .asList(OrderStatus.PROCESSING,
                        OrderStatus.SHIPPED,
                        OrderStatus.PENDING);
        return deliveryAssignmentRepository.
                findByDeliveryPersonIdAndDeliveryStatusIn(deliveryPersonId, activeStatuses);
    }

    // get all completed delivery assignments of delivery person

    public List<DeliveryAssignment> getCompletedOrdersOfDeliveryPerson(Long deliveryPersonId) {
        return deliveryAssignmentRepository.
                findByDeliveryPersonIdAndDeliveryStatus(deliveryPersonId, OrderStatus.DELIVERED);
    }


    // update order status

    public String updateOrderStatus(Long orderId, Long deliveryAssignmentId, OrderEvent event) {
        String result = orderService.updateOrderStatus(orderId, event);

        String afterState = OrderStatus.DELIVERED.name();

        DeliveryAssignment deliveryAssignment = deliveryAssignmentRepository.findById(deliveryAssignmentId)
                .orElseThrow(() -> new ItemNotFoundException("Delivery Assignment not found"));

        deliveryAssignment.setDeliveryStatus(OrderStatus.valueOf(afterState));

        String message = "Order status updated successfully to: " + afterState;

        if(result.equals(message)) {
            deliveryAssignmentRepository.save(deliveryAssignment);
            return message;
        } else {
            return result;
        }

    }
}
