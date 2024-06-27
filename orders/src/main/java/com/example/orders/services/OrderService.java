package com.example.orders.services;

import com.example.orders.model.Order;
import com.example.orders.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    private HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
    }

    //Metodo para obtener todas las ordenes
    public List<Order> getOrders() {
        return this.orderRepository.findAll();
    }

    //Metodo para obtener una orden por su id
    public Optional<Order> getOrderById(Long id) {
        return this.orderRepository.findById(id);
    }

    //Metodo para crear una nueva orden
    public ResponseEntity<Object> newOrder(Order orden) {
        Long productId = orden.getProductId();
        String url = "http://localhost:8083/api/products/" + productId;

        HttpHeaders headers = createHeaders("rubenUser", "ruben"); // Cambia esto según tus credenciales
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);

            // Verificar si el producto existe
            if (response.getStatusCode() == HttpStatus.OK) {
                // Producto encontrado, guardar la orden
                orderRepository.save(orden);
                return new ResponseEntity<>("Order created successfully", HttpStatus.CREATED);
            } else {
                // Producto no encontrado, no guardar la orden
                return new ResponseEntity<>("Product not found, order not created", HttpStatus.NOT_FOUND);
            }
        } catch (HttpClientErrorException.NotFound ex) {
            return new ResponseEntity<>("Product not found, order not created", HttpStatus.NOT_FOUND);
        }
    }

    //Metodo para eliminar una orden
    public ResponseEntity<Object> deleteOrder(Long id) {
        // Busca la orden por su id
        Optional <Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            // Elimina la orden si existe
            orderRepository.deleteById(id);
            return new ResponseEntity<>("Order deleted successfully", HttpStatus.OK);
        } else {
            // No elimina la orden si no existe
            return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
        }
    }

    //Metodo para actualizar una orden
    public ResponseEntity<Object> updateOrder(Long id, Order updatedOrder) {
        // Busca la orden por su id
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            // Actualiza la orden si existe
            Order order = optionalOrder.get();
            order.setOrderCode(updatedOrder.getOrderCode());
            order.setProductId(updatedOrder.getProductId());
            order.setUnitPrice(updatedOrder.getUnitPrice());
            order.setQuantity(updatedOrder.getQuantity());
            order.setTotal(updatedOrder.getTotal());
            order.setNotes(updatedOrder.getNotes());
            orderRepository.save(order);
            return new ResponseEntity<>("Order updated successfully", HttpStatus.OK);
        } else {
            // No actualiza la orden si no existe
            return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
        }
    }

}
