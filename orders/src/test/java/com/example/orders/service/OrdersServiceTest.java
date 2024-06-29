package com.example.orders.service;

import com.example.orders.model.Order;
import com.example.orders.repositories.OrderRepository;
import com.example.orders.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class OrdersServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetOrders() {
        Order order = new Order(1L, "Test312", 1L, 10.0, 1L, 10.0, "Test notes");
        List<Order> orders = Collections.singletonList(order);

        when(orderRepository.findAll()).thenReturn(orders);
        List<Order> result = orderService.getOrders();
        assertEquals(1, result.size());
        assertEquals(order, result.get(0));
    }

    @Test
    public void testNewOrder() {
        Order order = new Order(1L, "Test312", 1L, 10.0, 1L, 10.0, "Test notes");

        when(orderRepository.save(order)).thenReturn(order);

        // Mocking RestTemplate exchange method
        ResponseEntity<Object> productResponse = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class)))
                .thenReturn(productResponse);

        ResponseEntity<Object> response = orderService.newOrder(order);

        // Verificar que se guardó la orden
        verify(orderRepository, times(1)).save(order);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Verificar la respuesta
        Object responseBody = response.getBody();
        assertNotNull(responseBody);
        if (responseBody instanceof String) {
            assertEquals("Order created successfully", responseBody);
        } else if (responseBody instanceof HashMap) {
            @SuppressWarnings("unchecked")
            HashMap<String, Object> responseEntity = (HashMap<String, Object>) responseBody;
            assertEquals("Order created successfully", responseEntity.get("message"));
            assertEquals(order, responseEntity.get("data"));
        } else {
            fail("Unexpected response body type: " + responseBody.getClass().getName());
        }
    }

    @Test
    public void testDeleteOrder() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        ResponseEntity<Object> response = orderService.deleteOrder(orderId);

        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).deleteById(orderId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdateOrder() {
        Long orderId = 1L;
        Order order = new Order(orderId, "Test312", 1L, 10.0, 1L, 10.0, "Test notes");
        Order updatedOrder = new Order(orderId, "312Test", 1L, 10.0, 1L, 10.0, "Test notes updated");

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(updatedOrder);

        ResponseEntity<Object> response = orderService.updateOrder(orderId, updatedOrder);

        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(order);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("312Test", order.getOrderCode());
        assertEquals(1L, order.getProductId());
        assertEquals(10.0, order.getUnitPrice());
        assertEquals(1L, order.getQuantity());
        assertEquals(10.0, order.getTotal());
        assertEquals("Test notes updated", order.getNotes());
    }

    @Test
    public void testGetOrderById() {
        Long orderId = 1L;
        Order order = new Order(orderId, "Test312", 1L, 10.0, 1L, 10.0, "Test notes");

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        Optional<Order> result = orderService.getOrderById(orderId);

        assertTrue(result.isPresent());
        assertEquals(order, result.get());
    }


}
