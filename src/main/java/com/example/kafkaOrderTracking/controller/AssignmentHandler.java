package com.example.kafkaOrderTracking.controller;

import com.example.kafkaOrderTracking.entity.AssignedTruck;
import com.example.kafkaOrderTracking.entity.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
@RequiredArgsConstructor
public class AssignmentHandler {
    private final KafkaTemplate<String, Object> kafka;

    @KafkaListener(topics = "orders.created", groupId = "${DISPATCHER_GROUP_ID:dispatcher-grp}")
    public void onOrderCreated(Order order,@Header(KafkaHeaders.RECEIVED_KEY) String key) {

        //tır atama
        String truckId = "TRUCK-" + ThreadLocalRandom.current().nextInt(100,999);
        AssignedTruck assignedTruck = new AssignedTruck(
                truckId,
                order.getOrderId(),
                order.getLat(),
                order.getLon(),
                order.getDestinationLat(),
                order.getDestinationLon(),
                Instant.now()
        );

        kafka.send("orders.assigned",order.getOrderId(),assignedTruck);
        log.info("Assigned truck id {}", assignedTruck.getTruckId());
    }
}
