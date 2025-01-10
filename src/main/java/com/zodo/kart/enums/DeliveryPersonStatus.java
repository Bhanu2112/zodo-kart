package com.zodo.kart.enums;

/**
 * Author : Bhanu prasad
 */

public enum DeliveryPersonStatus {
    PENDING,                // Registered, awaiting admin approval
    ACTIVE,                 // Approved and available for deliveries
    INACTIVE,               // Not available for deliveries
    SUSPENDED,              // Temporarily suspended
    BANNED,                 // Permanently banned
    ON_DUTY,                // Currently making deliveries
    OFF_DUTY,               // Not on shift or not available at the moment
    VERIFICATION_REQUIRED,  // Needs additional verification (e.g., documents)
    UNDER_REVIEW,           // Admin is reviewing the delivery person
    DEACTIVATED,            // Voluntarily deactivated account
    TERMINATED              // Platform terminated the contract
}
