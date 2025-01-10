package com.zodo.kart.repository.order;

import com.zodo.kart.entity.order.DeliveryAssignment;
import com.zodo.kart.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author : Bhanu prasad
 */

@Repository
public interface DeliveryAssignmentRepository extends JpaRepository<DeliveryAssignment, Long> {
    List<DeliveryAssignment> findByDeliveryPersonIdAndDeliveryStatusIn(Long deliveryPersonId, List<OrderStatus> statuses);
    List<DeliveryAssignment> findByDeliveryPersonIdAndDeliveryStatus(Long deliveryPersonId, OrderStatus status);
}
