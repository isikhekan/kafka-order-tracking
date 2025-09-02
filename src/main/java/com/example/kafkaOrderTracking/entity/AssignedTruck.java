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
    private double originLat;
    private double originLon;
    private double destinationLat;
    private double destinationLon;
    private Instant assignedAt;
}
