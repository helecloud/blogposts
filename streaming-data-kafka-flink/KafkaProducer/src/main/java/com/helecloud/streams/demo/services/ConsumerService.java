package com.helecloud.streams.demo.services;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.helecloud.streams.demo.model.Vehicle;
import com.helecloud.streams.demo.model.VehicleStatistics;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ConsumerService {

  public static final String TOPIC = "vehicle-topic";


  @Async
  @KafkaListener(topics = {TOPIC})
  public void onVehicleRegistered(Vehicle vehicle) {

    log.info("Registered: {}", vehicle);
  }

}
