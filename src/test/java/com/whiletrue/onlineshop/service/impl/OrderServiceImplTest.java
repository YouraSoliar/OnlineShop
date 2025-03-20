package com.whiletrue.onlineshop.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.whiletrue.onlineshop.exception.OrderNotFoundException;
import com.whiletrue.onlineshop.model.Order;
import com.whiletrue.onlineshop.model.Product;
import com.whiletrue.onlineshop.model.User;
import com.whiletrue.onlineshop.repository.OrderRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order order;
    private User user;
    private Product product;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("yurii@gmail.com");
        user.setPassword("12345");
        user.setUsername("yurii");

        order = new Order();
        order.setId(1L);
        order.setUser(user);

        product = new Product();
        product.setId(100L);
        product.setTitle("Laptop");
    }

    @Test
    void testCreateOrder() {
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order createdOrder = orderService.createOrder(order);

        assertNotNull(createdOrder);
        assertEquals(1L, createdOrder.getId());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void testGetOrderById_OrderExists() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Optional<Order> retrievedOrder = orderService.getOrderById(1L);

        assertTrue(retrievedOrder.isPresent());
        assertEquals(1L, retrievedOrder.get().getId());
    }

    @Test
    void testGetOrderById_OrderNotFound() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Order> retrievedOrder = orderService.getOrderById(99L);

        assertFalse(retrievedOrder.isPresent());
    }

    @Test
    void testGetOrdersByUser() {
        when(orderRepository.findAllByUserEmail("yurii@gmail.com"))
                .thenReturn(Arrays.asList(order));

        List<Order> orders = orderService.getOrdersByUser("yurii@gmail.com");

        assertEquals(1, orders.size());
        assertEquals("yurii@gmail.com", orders.get(0).getUser().getEmail());
        verify(orderRepository).findAllByUserEmail("yurii@gmail.com");
    }

    @Test
    void testDeleteOrder() {
        doNothing().when(orderRepository).deleteById(1L);

        orderService.deleteOrder(1L);

        verify(orderRepository).deleteById(1L);
    }

    @Test
    void testAddProductToOrder_OrderExists() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        orderService.addProductToOrder(1L, product);

        assertTrue(order.getProducts().contains(product));
        verify(orderRepository).findById(1L);
        verify(orderRepository).save(order);
    }

    @Test
    void testAddProductToOrder_OrderNotFound() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class, () ->
                orderService.addProductToOrder(99L, product));

        assertEquals("Order not found with id 99", exception.getMessage());
        verify(orderRepository).findById(99L);
    }
}
