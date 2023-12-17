package dev.levelupschool.backend.service.implementation;

import com.google.gson.Gson;
import com.squareup.okhttp.*;
import dev.levelupschool.backend.data.dto.request.PaymentDetails;
import dev.levelupschool.backend.exception.PaymentProcessingException;
import dev.levelupschool.backend.service.interfaces.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class LevelUpPaymentService implements PaymentService {
    @Value("${payments.gatewayUrl}")
    private String paymentGatewayUrl;
    @Value("${payments.gatewayPort}")
    private String paymentGatewayPort;
    @Value("${PREMIUM_PRICE}")
    private String price;
    private Gson gson = new Gson();

    @Override
    public boolean processPayment(PaymentDetails paymentDetails) {
        OkHttpClient okHttpClient = new OkHttpClient();

        Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("card_holder", paymentDetails.getCardHolder());
        requestBodyMap.put("card_number", paymentDetails.getCardNumber());
        requestBodyMap.put("card_cvv", paymentDetails.getCardCvv());
        requestBodyMap.put("price", price);


        String json = gson.toJson(requestBodyMap);


        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);


        Request request = new Request.Builder()
            .url(paymentGatewayUrl + ":" + paymentGatewayPort + "/payments")
            .post(body)
            .build();

        try {

            Response response = okHttpClient.newCall(request).execute();


            if (response.isSuccessful()) {
                return true;
            } else {
                String responseBody = response.body().string();
                log.error("Payment processing failed: " + responseBody);
                String errorMessage = extractErrorMessage(responseBody);
                throw new PaymentProcessingException("Payment processing failed: " + errorMessage, HttpStatus.valueOf(response.code()));
            }
        } catch (IOException e) {
            log.info("Error "+ e.getMessage());
            throw new PaymentProcessingException("Payment processing failed: " + e.getMessage(), HttpStatus.valueOf(500));


        }
    }

    private String extractErrorMessage(String responseBody) {
        try {
            Map<String, String> responseMap = gson.fromJson(responseBody, Map.class);
            return responseMap.getOrDefault("detail", "Unknown error");
        } catch (Exception e) {

            return responseBody;
        }
    }

}
