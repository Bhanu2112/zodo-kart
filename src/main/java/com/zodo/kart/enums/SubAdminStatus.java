package com.zodo.kart.enums;

/**
 * Author : Bhanu prasad
 */

public enum SubAdminStatus {
    ACTIVE,                // Sub Admin is fully active
    INACTIVE,              // Sub Admin is inactive but still part of the system
    SUSPENDED,             // Temporarily suspended for violations or other reasons
    BANNED,                // Permanently banned for severe infractions
    PENDING_APPROVAL,      // Waiting for approval by a higher-level admin or system
    DEACTIVATED,           // Sub Admin voluntarily deactivated their account
    UNDER_REVIEW,          // Sub Admin account is under investigation
    RESTRICTED             // Sub Admin has limited access due to certain conditions
}


