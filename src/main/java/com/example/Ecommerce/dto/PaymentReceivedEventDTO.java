package com.example.Ecommerce.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentReceivedEventDTO extends EventDTO {
    private double amountPaid;
}
