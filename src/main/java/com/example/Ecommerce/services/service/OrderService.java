package com.example.Ecommerce.services.service;

import com.example.Ecommerce.dto.EventDTO;

public interface OrderService {
    void processEvent(EventDTO event);
}
