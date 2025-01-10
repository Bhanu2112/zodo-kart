package com.zodo.kart.enums;

public enum OrderStatus {
    INITIATED,     // Order has been initiated by the user but not yet confirmed
    PENDING,       // The order has been confirmed by the user but is awaiting seller action.
    ACCEPTED,      // Order has been accepted by the seller
    COMPLETED,     // Order has been completed successfully
    CANCELLED,     // Order has been cancelled
    PROCESSING,    // Order is currently being processed
    SHIPPED,       // Order has been shipped
    DELIVERED,     // Order has been delivered to the customer
    RETURNED,      // Order has been returned by the customer
    REFUNDED,      // Payment has been refunded for the order
    PAYMENT_ERROR  // Payment error has occorse or payment failed
}