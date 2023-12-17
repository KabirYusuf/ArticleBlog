package dev.levelupschool.backend.service.interfaces;

import dev.levelupschool.backend.data.dto.request.PaymentDetails;

public interface PaymentService {
    boolean processPayment(PaymentDetails paymentDetails);
}
