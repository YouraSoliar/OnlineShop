package com.whiletrue.onlineshop.service.impl;

import com.whiletrue.onlineshop.model.Order;
import com.whiletrue.onlineshop.repository.OrderRepository;
import com.whiletrue.onlineshop.service.OrderService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder(Order order) {
        Order createdOrder = orderRepository.save(order);
        logger.info("Order created successfully with ID: {}", createdOrder.getId());
        return createdOrder;
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        Optional<Order> getOrder = orderRepository.findById(id);
        logger.info("Is order with ID: {} - {}", id, getOrder.isPresent());
        return getOrder;
    }

    @Override
    public List<Order> getOrdersByUser(String userEmail) {
        return orderRepository.findAllByUserEmail(userEmail);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
        logger.info("Order with ID {} deleted successfully", id);
    }
}
