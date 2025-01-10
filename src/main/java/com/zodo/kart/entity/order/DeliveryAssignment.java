package com.zodo.kart.entity.order;

import com.zodo.kart.entity.users.DeliveryPerson;
import com.zodo.kart.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Author : Bhanu prasad
 */

@Entity
@Table(name = "DELIVERY_ASSIGNMENT")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

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
