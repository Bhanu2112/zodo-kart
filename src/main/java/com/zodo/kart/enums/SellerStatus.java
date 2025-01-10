package com.zodo.kart.enums;

/**
 * Author : Bhanu prasad
 */

public enum SellerStatus {
    PENDING,              // Seller registered, awaiting approval
    ACTIVE,               // Seller is approved and active
    INACTIVE,             // Seller is inactive (not currently selling)
    SUSPENDED,            // Seller account temporarily suspended
    REJECTED,             // Seller application rejected
    BANNED,               // Seller is permanently banned
    VERIFICATION_REQUIRED, // Additional verification needed
    UNDER_REVIEW,         // Admin is reviewing the seller
    DEACTIVATED           // Seller voluntarily deactivates account
}