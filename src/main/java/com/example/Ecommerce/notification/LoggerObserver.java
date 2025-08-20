package com.example.Ecommerce.notification;

import com.example.Ecommerce.dto.EventDTO;
import com.example.Ecommerce.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class LoggerObserver implements  Observer{

    @Override
    public void update(EventDTO event, Order order){
        System.out.println("[LOG] Event processed:" + event.getEventType()
                            + " for Order ID: " + order.getOrderId()
                            + ". New status: " + order.getStatus());
    }
}
