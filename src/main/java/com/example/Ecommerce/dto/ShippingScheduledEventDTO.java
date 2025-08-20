package com.example.Ecommerce.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = true)
public class ShippingScheduledEventDTO extends EventDTO{
    private Instant shippingDate;
}
