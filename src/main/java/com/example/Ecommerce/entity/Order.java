package com.example.Ecommerce.entity;


import com.example.Ecommerce.enums.OrderStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    private String orderId;
    private String customerId;
    private List<Object> items;
    private double totalAmount;
    private OrderStatus status;

}
