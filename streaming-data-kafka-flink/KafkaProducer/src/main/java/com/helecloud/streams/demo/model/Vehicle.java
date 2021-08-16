package com.helecloud.streams.demo.model;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Vehicle {

  private String plateNumber;

  private String vignetteStickerId;

  private Instant timestamp;

  private VehicleType vehicleType;

}
