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
    private double lat;
    private double lon;
    private double destinationLat;
    private double destinationLon;
    private Instant createdAt;
}

