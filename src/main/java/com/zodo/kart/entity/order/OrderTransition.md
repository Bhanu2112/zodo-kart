# Order Status State Machine

## 1. INITIATED to PENDING (Order Confirmed)
- **Scenario**: When the user confirms the order and payment is initiated, the status changes from `INITIATED` to `PENDING`.
- **Event Triggered**: `ORDER_CONFIRMED`
- **Description**: The order has been created but is awaiting the seller's acceptance. This state means the order is confirmed by the user but is still pending action from the seller.

## 2. PENDING to ACCEPTED (Order Accepted by Seller)
- **Scenario**: The seller accepts the order.
- **Event Triggered**: `ORDER_ACCEPTED`
- **Description**: Once the seller reviews and accepts the order, it moves from `PENDING` to `ACCEPTED`. This indicates that the seller has agreed to fulfill the order.

## 3. ACCEPTED to PROCESSING
- **Scenario**: The seller begins processing the order.
- **Event Triggered**: `ORDER_PROCESSING`
- **Description**: The order is now actively being prepared or packed by the seller. This state signifies that the seller is working on fulfilling the order requirements.

## 4. PROCESSING to SHIPPED
- **Scenario**: The seller marks the order as ready for shipment.
- **Event Triggered**: `ORDER_SHIPPED`
- **Description**: Once the order is prepared and handed over for delivery, its status changes to `SHIPPED`. This indicates that the order is on its way to the customer.

## 5. SHIPPED to DELIVERED
- **Scenario**: The delivery person delivers the order to the customer.
- **Event Triggered**: `ORDER_DELIVERED`
- **Description**: This state indicates that the order has reached the customer and has been successfully delivered.

## 6. DELIVERED to COMPLETED
- **Scenario**: The order is marked as completed after successful delivery.
- **Event Triggered**: `ORDER_COMPLETED`
- **Description**: This final state means the order is officially closed, and all processes related to it have been successfully completed.

## 7. PENDING to CANCELLED (Order Cancelled by User)
- **Scenario**: The user or seller cancels the order before it's accepted.
- **Event Triggered**: `ORDER_CANCELLED`
- **Description**: If the user decides to cancel the order before the seller takes any action, it moves to the `CANCELLED` state. No further processing will be done.

## 8. DELIVERED to RETURNED (Order Returned by Customer)
- **Scenario**: The customer decides to return the delivered order.
- **Event Triggered**: `ORDER_RETURNED`
- **Description**: If the customer is not satisfied or wants to return the delivered order, the state changes to `RETURNED`.

## 9. RETURNED to REFUNDED (Refund Process Initiated)
- **Scenario**: The refund process is initiated after the order is returned.
- **Event Triggered**: `ORDER_REFUNDED`
- **Description**: After a successful return of the product, the payment made by the customer is refunded, and the order status moves to `REFUNDED`.

## 10. INITIATED to PAYMENT_ERROR (Payment Failure)
- **Scenario**: If the payment fails during the initial phase.
- **Event Triggered**: `PAYMENT_FAILED`
- **Description**: In the case of a payment failure, the order status moves to `PAYMENT_ERROR`, indicating that the order could not be processed due to a payment issue.
