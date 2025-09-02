package com.example.kafkaOrderTracking.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserOrder {
    @NotBlank
    private String customerId;
    @NotNull
    private double lat;
    @NotNull
    private double lon;
    @NotNull
    private double destinationLat;
    @NotNull
    private double destinationLon;
}
