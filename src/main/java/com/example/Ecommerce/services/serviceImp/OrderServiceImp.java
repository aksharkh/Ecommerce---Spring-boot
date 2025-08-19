package com.example.Ecommerce.services.serviceImp;

import com.example.Ecommerce.dto.EventDTO;
import com.example.Ecommerce.dto.OrderCreatedEventDTO;
import com.example.Ecommerce.dto.PaymentReceivedEventDTO;
import com.example.Ecommerce.entity.Order;
import com.example.Ecommerce.enums.OrderStatus;
import com.example.Ecommerce.notification.Observer;
import com.example.Ecommerce.repository.OrderRepository;
import com.example.Ecommerce.services.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderServiceImp implements OrderService {


    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final List<Observer> observers;

    private void notifyObservers(EventDTO event, Order order) {
        observers.forEach(observer -> observer.update(event,order));

    }

    @Override
    public void processEvent(EventDTO event) {
        // Using a switch on the enum is clean and type-safe
        switch (event.getEventType()) {
            case OrderCreated:
                handleOrderCreated((OrderCreatedEventDTO) event);
                break;
            case PaymentReceived:
                handlePaymentReceived((PaymentReceivedEventDTO) event);
                break;
            default:
                System.out.println("[WARN] Unknown event type: " + event.getEventType());
        }
    }

    private void handleOrderCreated(OrderCreatedEventDTO event) {
        Order order = modelMapper.map(event, Order.class);
        order.setStatus(OrderStatus.PENDING);
        Order savedOrder = orderRepository.save(order);
        notifyObservers(event, savedOrder);
    }

    private void handlePaymentReceived(PaymentReceivedEventDTO event) {
        orderRepository.findById(event.getOrderId()).ifPresentOrElse(order -> {
            if (event.getAmountPaid() >= order.getTotalAmount()) {
                order.setStatus(OrderStatus.PAID);
            } else {
                order.setStatus(OrderStatus.PARTIALLY_PAID);
            }
            Order savedOrder = orderRepository.save(order);
            notifyObservers(event, savedOrder);
        }, () -> System.out.println("[ERROR] Payment for non-existent order: " + event.getOrderId()));
    }
}
