package com.example.kafkaOrderTracking.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private String orderId;
    private String userId;
    private String lat;
    private String lon;
    private String destinationLat;
    private String destinationLon;
    private Instant createdAt;
}

