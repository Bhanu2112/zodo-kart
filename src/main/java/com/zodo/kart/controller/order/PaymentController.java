package com.zodo.kart.controller.order;

import com.zodo.kart.entity.order.Payment;
import com.zodo.kart.service.order.OrderService;
import com.zodo.kart.service.order.PhonePeService;
import com.zodo.kart.service.sellerorder.OperatorOrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Author : Bhanu prasad
 */

@RestController
@RequestMapping("/public/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PhonePeService phonePeService;
    private final OrderService orderService;
    private final OperatorOrderService operatorOrderService;


    @PostMapping("/callback")
    public ResponseEntity<String> handleCallback(HttpServletRequest httpServletRequest,
                                                 @RequestHeader(value = "Cookie", required = false) String cookieHeader,
                                                 @RequestHeader("x-verify") String xVerify,
                                                 @RequestBody String response) {
        // Log the callback data
        System.out.println("Received Callback:");
        System.out.println("x-verify: " + xVerify);
        System.out.println("Response: " + response);




        // Here, you can process the response and verify it if necessary
        // For example:
        boolean isValid = phonePeService.verifyResponse(xVerify, response);
        if (isValid) {
            // Process the valid callback (update order status, etc.)
            System.out.println("Callback received and verified");

            // Update order payment status
            orderService.updateOrderPaymentStatusAfterPayment(response);

            return ResponseEntity.ok("Callback received and verified");
        } else {
            System.out.println("Invalid callback");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid callback");
        }
    }

    @PostMapping("/operator/callback")
    public ResponseEntity<String> handleOperatorCallback(HttpServletRequest httpServletRequest,
                                                 @RequestHeader(value = "Cookie", required = false) String cookieHeader,
                                                 @RequestHeader("x-verify") String xVerify,
                                                 @RequestBody String response) {
        // Log the callback data
        System.out.println("Received Callback:");
        System.out.println("x-verify: " + xVerify);
        System.out.println("Response: " + response);




        // Here, you can process the response and verify it if necessary
        // For example:
        boolean isValid = phonePeService.verifyResponse(xVerify, response);
        if (isValid) {
            // Process the valid callback (update order status, etc.)
            System.out.println("Callback received and verified");

            // Update order payment status
            operatorOrderService.updateOrderPaymentStatusAfterPayment(response);

            return ResponseEntity.ok("Callback received and verified");
        } else {
            System.out.println("Invalid callback");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid callback");
        }
    }


    @PostMapping("/confirmation")
    public String handleRedirect(@RequestParam(value = "code", required = false) String code,
                                 @RequestParam(value = "transactionId", required = false) String transactionId,
                                 @RequestParam(value = "amount", required = false) String amount,
                                 @RequestParam(value = "checksum", required = false) String checksum,
                                 @RequestParam Map<String, String> allParams,
                                 HttpServletRequest request) {

        // Log all received parameters for debugging
        System.out.println("Received Redirect Data: " + allParams);

        // Get the xVerify header from the request (used for verification)
        String xVerify = request.getHeader("x-verify");

        // Verify the response using the PhonePe client's built-in method
        // boolean isResponseValid = phonePeService.verifyResponse(xVerify, allParams.toString());

        // Process the payment status
        if ("PAYMENT_SUCCESS".equals(code)) {
            // Handle successful payment logic here
            System.out.println("Payment Successful! Transaction ID: " + transactionId + ", Amount: " + amount);
            return "Payment successful! Thank you for your purchase.";
        } else {
            // Handle payment failure logic here
            System.out.println("Payment Failed or Pending. Please retry or contact support.");
            return "Payment failed or pending. Please retry or contact support.";
        }
    }

    @PostMapping("/operator/confirmation")
    public String handleOperatorRedirect(@RequestParam(value = "code", required = false) String code,
                                 @RequestParam(value = "transactionId", required = false) String transactionId,
                                 @RequestParam(value = "amount", required = false) String amount,
                                 @RequestParam(value = "checksum", required = false) String checksum,
                                 @RequestParam Map<String, String> allParams,
                                 HttpServletRequest request) {

        // Log all received parameters for debugging
        System.out.println("Received Redirect Data: " + allParams);

        // Get the xVerify header from the request (used for verification)
        String xVerify = request.getHeader("x-verify");

        // Verify the response using the PhonePe client's built-in method
        // boolean isResponseValid = phonePeService.verifyResponse(xVerify, allParams.toString());

        // Process the payment status
        if ("PAYMENT_SUCCESS".equals(code)) {
            // Handle successful payment logic here
            System.out.println("Payment Successful! Transaction ID: " + transactionId + ", Amount: " + amount);
            return "Payment successful! Thank you for your purchase.";
        } else {
            // Handle payment failure logic here
            System.out.println("Payment Failed or Pending. Please retry or contact support.");
            return "Payment failed or pending. Please retry or contact support.";
        }
    }


    // get all payments

    @GetMapping("/all-payments")
    public List<Payment> getAllPayments() {
        return orderService.getAllPayments();
    }

    // get payment by id

    @GetMapping("/payment/{id}")
    public Payment getPaymentById(@PathVariable Long id) {
        return orderService.getPaymentById(id);
    }



}
