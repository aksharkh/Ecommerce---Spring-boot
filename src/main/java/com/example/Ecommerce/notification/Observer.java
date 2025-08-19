package com.example.Ecommerce.notification;

import com.example.Ecommerce.dto.EventDTO;
import com.example.Ecommerce.entity.Order;

public interface Observer {
    void update(EventDTO event, Order order);
}
