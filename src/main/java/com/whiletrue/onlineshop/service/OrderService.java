package com.whiletrue.onlineshop.service;

import com.whiletrue.onlineshop.model.Order;
import com.whiletrue.onlineshop.model.Product;
import java.util.List;
import java.util.Optional;

public interface OrderService {
    Order createOrder(Order order);
    Optional<Order> getOrderById(Long id);
    List<Order> getOrdersByUser(String userEmail);
    void deleteOrder(Long id);
    void addProductToOrder(Long orderId, Product product);
}
