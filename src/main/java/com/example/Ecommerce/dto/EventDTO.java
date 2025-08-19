package com.example.Ecommerce.dto;


import com.example.Ecommerce.enums.EventType;
import lombok.Data;

import java.time.Instant;

@Data
public class EventDTO {
    private String eventId;
    private Instant timestamp;
    private EventType eventType;
    private String orderId;


}
