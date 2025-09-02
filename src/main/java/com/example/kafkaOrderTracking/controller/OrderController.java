package com.example.kafkaOrderTracking.controller;


import com.example.kafkaOrderTracking.entity.Order;
import com.example.kafkaOrderTracking.entity.UserOrder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final KafkaTemplate<String, Object> kafka;

    @PostMapping(path = "/order")
    ResponseEntity<Map<String, String>> create(@Valid @RequestBody UserOrder userOrder) {
        String orderId = UUID.randomUUID().toString();
        Order order = new Order(
                orderId,
                userOrder.getCustomerId(),
                userOrder.getLat(),
                userOrder.getLon(),
                userOrder.getDestinationLat(),
                userOrder.getDestinationLon(),
                Instant.now()
        );
        kafka.send("orders.created",orderId,order);
        return ResponseEntity.ok().body(Map.of("orderId", orderId));


    }
}
