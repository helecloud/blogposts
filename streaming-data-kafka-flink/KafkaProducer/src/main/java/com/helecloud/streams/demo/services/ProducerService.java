package com.helecloud.streams.demo.services;

import static com.helecloud.streams.demo.model.VehicleType.CAR;
import static com.helecloud.streams.demo.model.VehicleType.TRUCK;
import static java.util.UUID.randomUUID;

import java.time.Instant;
import java.util.Random;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.helecloud.streams.demo.model.Vehicle;
import com.helecloud.streams.demo.model.VehicleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProducerService {

  private final KafkaTemplate<String, Vehicle> kafkaTemplate;

  private static final String TOPIC = "vehicle-topic";

  @Scheduled(fixedDelay= 2000)
  public void produce() {

    Vehicle vehicle = generateRandomVehicle();

    log.info("Vehicle: {}", vehicle);

    kafkaTemplate.send(TOPIC, vehicle);
  }

  private Vehicle generateRandomVehicle() {

    VehicleType vehicleType = CAR;

    Random rand = new Random();

    if(rand.nextInt(101) % 10 == 0) {
      vehicleType = TRUCK;
    }

    return new Vehicle(randomUUID().toString(), randomUUID().toString(), Instant.now(), vehicleType);
  }

}
