package com.example.Ecommerce.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrderCancelledEventDTO extends  EventDTO{
    private  String reason;
}
