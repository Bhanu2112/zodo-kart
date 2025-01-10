package com.zodo.kart.service.order;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.sdk.pg.common.http.PhonePeResponse;
import com.phonepe.sdk.pg.payments.v1.PhonePePaymentClient;
import com.phonepe.sdk.pg.payments.v1.models.request.PgPayRequest;
import com.phonepe.sdk.pg.payments.v1.models.response.PgPayResponse;
import com.phonepe.sdk.pg.payments.v1.models.response.PgTransactionStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Author : Bhanu prasad
 */

@Service
@RequiredArgsConstructor
public class PhonePeService {

    private final PhonePePaymentClient phonePeClient;

    @Value("${phonepe.pg.merchantId}")
    private String merchantId;

    private String redirectMode = "POST";

    public PhonePeResponse<PgPayResponse> initiatePayment(double amount, String redirectUrl, String callbackUrl ,String merchantTransactionId, Long orderId , Long userId){


        PgPayRequest pgPayRequest = PgPayRequest.PayPagePayRequestBuilder()
                .amount((long) (amount*100))
                .merchantId(merchantId)
                .merchantTransactionId(merchantTransactionId)
                .merchantOrderId(orderId.toString())
                .callbackUrl(callbackUrl)
                .merchantUserId(userId.toString())
                .redirectUrl(redirectUrl)
                .redirectMode(redirectMode)
                .build();

        try {
            PhonePeResponse<PgPayResponse> res = phonePeClient.pay(pgPayRequest);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initiate payment: " + e.getMessage());
        }
    }



    public PhonePeResponse<PgPayResponse> initiateOperatorPayment(double amount, String redirectUrl, String callbackUrl ,String merchantTransactionId, Long orderId , String operatorId){


        PgPayRequest pgPayRequest = PgPayRequest.PayPagePayRequestBuilder()
                .amount((long) (amount*100))
                .merchantId(merchantId)
                .merchantTransactionId(merchantTransactionId)
                .merchantOrderId(orderId.toString())
                .callbackUrl(callbackUrl)
                .merchantUserId(operatorId)
                .redirectUrl(redirectUrl)
                .redirectMode(redirectMode)
                .build();

        try {
            PhonePeResponse<PgPayResponse> res = phonePeClient.pay(pgPayRequest);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initiate payment: " + e.getMessage());
        }
    }

    public boolean verifyResponse(String xVerify, String response){
        return  phonePeClient.verifyResponse(xVerify, response);
    }

    // decode base64 response

    public JsonNode decodeBase64Response(String base64EncodedString){
        byte[] decodedBytes = Base64.getDecoder().decode(base64EncodedString);

        String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);


        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Map<String, Object> payload = objectMapper.readValue(decodedString, Map.class);
            JsonNode jsonNode = objectMapper.readTree(decodedString);
            System.out.println("Decoded and Parsed JSON: " + jsonNode);
            return jsonNode;
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(decodedString);
        throw new RuntimeException("Failed to decode and parse JSON");
    }


    // check status of payment

    public PhonePeResponse<PgTransactionStatusResponse> checkPaymentStatus(String merchantTransactionId){
        return phonePeClient.checkStatus(merchantTransactionId);
    }




}
