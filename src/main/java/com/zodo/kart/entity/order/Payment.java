package com.zodo.kart.entity.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zodo.kart.enums.PaymentMethods;
import com.zodo.kart.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Author : Bhanu prasad
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId; // Unique identifier for the payment entry
    private String merchantTransactionId; // Same as paymentId, unique transaction ID
    private String providerReferenceId; // Payment gateway transaction ID

    private double paymentAmount; // Amount paid for the order
    //  private String paymentStatus; // Status of payment (e.g., SUCCESS, FAILED, PENDING)
    private LocalDateTime paymentDate; // Date of the payment transaction

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus; // Status of payment (e.g., PENDING, SUCCESS, FAILED)

    @Enumerated(EnumType.STRING)
    private PaymentMethods paymentMethod; // Payment method used for the order (e.g., NETBANKING, UPI)



    @OneToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    @OneToOne
    @JoinColumn(name = "operator_order_id")
    @JsonIgnore
    private OperatorOrder operatorOrder;
}
