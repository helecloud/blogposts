package com.helecloud.streams.demo.serialization;

import org.apache.flink.api.common.serialization.SerializationSchema;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.helecloud.streams.demo.model.VehicleStatistics;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@RequiredArgsConstructor
@Log4j2
public class VehicleStatisticsSerializationSchema implements SerializationSchema<VehicleStatistics> {

  private final ObjectMapper objectMapper;

  @Override
  public byte[] serialize(VehicleStatistics vehicleStatistics) {
    try {
      objectMapper.registerModule(new JavaTimeModule());

      return objectMapper.writeValueAsString(vehicleStatistics).getBytes();

    } catch (com.fasterxml.jackson.core.JsonProcessingException e) {

      log.error("Failed to parse JSON", e);

    }

    return new byte[0];
  }
}
