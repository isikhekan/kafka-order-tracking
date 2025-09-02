package com.example.kafkaOrderTracking.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserOrder {
    @NotBlank
    private String customerId;
    @NotBlank
    private String lat;
    @NotBlank
    private String lon;
    @NotBlank
    private String destinationLat;
    @NotBlank
    private String destinationLon;
}
