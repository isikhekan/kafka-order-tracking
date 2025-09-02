package com.example.kafkaOrderTracking.controller;

import com.example.kafkaOrderTracking.entity.AssignedTruck;
import com.example.kafkaOrderTracking.entity.OrderCompleted;
import com.example.kafkaOrderTracking.entity.TruckLocation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrackerController {
    private final KafkaTemplate<String, Object> kafka;

    //Aktif tripler
    private final Map<String,TripState> trips = new ConcurrentHashMap<>();

    @KafkaListener(
            topics = "orders.assigned",
            groupId = "${TRACKER_GROUP_ID:tracker-grp}",
            properties = {
                    "spring.json.value.default.type=com.example.kafkaOrderTracking.entity.AssignedTruck"
            }
    )
    public void onAssigned(AssignedTruck assignedTruck,
                           @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        trips.put(assignedTruck.getOrderId(),TripState.from(assignedTruck));
        log.info("Tracker Started for orderId:{}", assignedTruck.getOrderId());
    }

    @Scheduled(fixedDelay = 2000)
    public void tick(){
        trips.values().forEach(this::advanceAndPublish);
    }

    public void advanceAndPublish(TripState t){
        if (t.completed) return;

        double step = 0.05;
        t.progress  = Math.min(1.0,t.progress+step);
        double lat = lerp(t.originLat,t.destinationLat,t.progress);
        double lon = lerp(t.originLon,t.destinationLon,t.progress);

        TruckLocation truckLocation = new TruckLocation(
                t.truckId,t.orderId,lat,lon, Instant.now());
        kafka.send("orders.location",t.orderId,truckLocation);
        log.info("orders.location → orderId={} truckId={} lat={} lon={}",
                t.orderId, t.truckId, lat, lon);

        if(t.progress >=1.0) {
            t.completed = true;
            OrderCompleted orderCompleted = new OrderCompleted(
                    t.orderId,t.truckId,Instant.now()
            );
            kafka.send("orders.completed",t.orderId,orderCompleted);
            log.info("orders.completed → orderId={} truckId={}", t.orderId, t.truckId);
            trips.remove(t.orderId);
        }

    }



    private static double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }

    private static class TripState {
        String orderId;
        String truckId;
        double originLat,originLon;
        double destinationLat,destinationLon;
        double progress;
        boolean completed;


        static TripState from (AssignedTruck assignedTruck) {
            TripState tripState = new TripState();
            tripState.orderId = assignedTruck.getOrderId();
            tripState.truckId = assignedTruck.getTruckId();
            tripState.originLat = assignedTruck.getOriginLat();
            tripState.originLon = assignedTruck.getOriginLon();
            tripState.destinationLat = assignedTruck.getDestinationLat();
            tripState.destinationLon = assignedTruck.getDestinationLon();
            tripState.progress = 0;
            tripState.completed = false;
            return tripState;

        }
    }

}
