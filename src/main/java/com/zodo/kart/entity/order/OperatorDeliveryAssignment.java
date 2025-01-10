package com.zodo.kart.entity.order;

import com.zodo.kart.entity.users.DeliveryPerson;
import com.zodo.kart.enums.OrderStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Author : Bhanu prasad
 */

@Entity
@Table(name = "operator_delivery_assignment")
public class OperatorDeliveryAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OperatorOrder order;

    @ManyToOne
    @JoinColumn(name = "delivery_person_id", nullable = false)
    private DeliveryPerson deliveryPerson;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status")
    private OrderStatus deliveryStatus;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}
