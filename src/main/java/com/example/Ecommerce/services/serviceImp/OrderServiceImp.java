package com.example.Ecommerce.services.serviceImp;

import com.example.Ecommerce.dto.*;
import com.example.Ecommerce.entity.Order;
import com.example.Ecommerce.entity.OrderItem;
import com.example.Ecommerce.enums.OrderStatus;
import com.example.Ecommerce.notification.Observer;
import com.example.Ecommerce.repository.OrderRepository;
import com.example.Ecommerce.services.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


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

        switch (event.getEventType()) {
            case OrderCreated:
                handleOrderCreated((OrderCreatedEventDTO) event);
                break;
            case PaymentReceived:
                handlePaymentReceived((PaymentReceivedEventDTO) event);
                break;
            case ShippingScheduled:
                handleShippingScheduled((ShippingScheduledEventDTO) event);
                break;
            case OrderCancelled:
                handleOrderCancelled((OrderCancelledEventDTO) event);
                break;

            default:
                System.out.println("[WARN] Unknown event type: " + event.getEventType());
        }
    }

    private void handleOrderCreated(OrderCreatedEventDTO event) {
        Order order = modelMapper.map(event, Order.class);
        order.setStatus(OrderStatus.PENDING);
        if (event.getItems() != null) {
            List<OrderItem> orderItems = event.getItems().stream()
                    .map(itemDTO -> {
                        OrderItem orderItem = modelMapper.map(itemDTO, OrderItem.class);
                        orderItem.setOrder(order);
                        return orderItem;
                    }).collect(Collectors.toList());
            order.setItems(orderItems);
        }
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

    private void handleShippingScheduled(ShippingScheduledEventDTO event){
        orderRepository.findById(event.getOrderId()).ifPresentOrElse(order -> {
            order.setStatus(OrderStatus.SHIPPED);
            Order savedOrder = orderRepository.save(order);
            System.out.println("Order "+order.getOrderId()+ " has been shipped.");
            notifyObservers(event , savedOrder);
        }, () -> System.out.println("[ERROR] Shipping for non-existing order:" + event.getOrderId()));
    }

    private void handleOrderCancelled(OrderCancelledEventDTO event){
        orderRepository.findById(event.getOrderId()).ifPresentOrElse(order -> {
            order.setStatus(OrderStatus.CANCELLED);
            Order cancelledOrder = orderRepository.save(order);
            System.out.println("Order :"+ order.getOrderId()+ " was cancelled. Reason: " + event.getReason());
            notifyObservers(event, cancelledOrder);
        }, () -> System.out.println("[ERROR] Cancellation for non-existing order:"+ event.getOrderId()));
    }
}
