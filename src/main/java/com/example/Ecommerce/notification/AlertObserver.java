package com.example.Ecommerce.notification;


import com.example.Ecommerce.dto.EventDTO;
import com.example.Ecommerce.entity.Order;
import com.example.Ecommerce.enums.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class AlertObserver implements Observer{
    @Override
    public void update(EventDTO event, Order order){
        if(order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.SHIPPED) {
            System.out.println(">>[ALERT] Critical update for Order "+ order.getOrderId()
                                + ": Status changed to " + order.getStatus());
        }
    }
}
