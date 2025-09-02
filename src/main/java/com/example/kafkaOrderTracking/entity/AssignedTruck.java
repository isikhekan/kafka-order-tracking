package com.example.kafkaOrderTracking.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AssignedTruck {
    private String truckId;
    private String orderId;
    private String originLat;
    private String originLon;
    private String destinationLat;
    private String destinationLon;
    private Instant assignedAt;
}
