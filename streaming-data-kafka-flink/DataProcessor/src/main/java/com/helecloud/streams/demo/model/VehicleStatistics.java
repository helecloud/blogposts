package com.helecloud.streams.demo.model;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class VehicleStatistics {

  @JsonProperty("start")
  private Instant start;

  @JsonProperty("end")
  private Instant end;

  @JsonProperty("vehicleType")
  private VehicleType vehicleType;

  @JsonProperty("count")
  private Integer count;

}
