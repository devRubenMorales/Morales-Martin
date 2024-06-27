package com.example.orders.controllers;

import com.example.orders.model.Order;
import com.example.orders.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.support.DefaultMessageSourceResolvable;


import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(path = "/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    //Controlador para obtener todas las ordenes
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Order> getOrders() {
        return this.orderService.getOrders();
    }

    //Controlador para obtener una orden por su id
    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Order getOrderById(Long id) {
        return this.orderService.getOrderById(id).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    //Controlador para crear una nueva orden
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> newOrder(@Valid @RequestBody Order orden, BindingResult bindingResult) {
        System.out.println("Probando crear orden");
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        return orderService.newOrder(orden);
    }

    //Controlador para eliminar una orden
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> deleteOrder(@PathVariable Long id) {
        return this.orderService.deleteOrder(id);
    }

    //Controlador para actualizar una orden
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> updateOrder(@PathVariable("id") Long id, @Valid @RequestBody Order updatedOrder, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        return orderService.updateOrder(id, updatedOrder);
    }
}
