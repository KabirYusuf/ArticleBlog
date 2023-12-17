package dev.levelupschool.backend.data.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaymentDetails {
    private String cardHolder;
    private String cardNumber;
    private String cardCvv;
}
