package com.example.Ecommerce.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
public class OrderCreatedEventDTO extends EventDTO {
    private String customerId;
    private List<Object> items;
    private double totalAmount;
}
