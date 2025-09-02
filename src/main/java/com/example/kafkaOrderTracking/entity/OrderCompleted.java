package com.example.kafkaOrderTracking.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderCompleted {
    private String orderId;
    private String truckId;
    private Instant completedAt;
}
