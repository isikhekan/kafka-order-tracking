package com.example.kafkaOrderTracking.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TruckLocation {
    private String truckId;
    private String orderId;
    private double lat;
    private double lon;
    private Instant at;
}
