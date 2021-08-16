package com.helecloud.streams.demo.serialization;

import java.io.IOException;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.helecloud.streams.demo.model.Vehicle;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VehicleDeserializationSchema implements DeserializationSchema<Vehicle> {

  private final ObjectMapper objectMapper;

  @Override
  public Vehicle deserialize(byte[] bytes) throws IOException {

    objectMapper.registerModule(new JavaTimeModule());

    return objectMapper.readValue(bytes, Vehicle.class);
  }

  @Override
  public boolean isEndOfStream(Vehicle vehicle) {
    return false;
  }

  @Override
  public TypeInformation<Vehicle> getProducedType() {
    return TypeInformation.of(Vehicle.class);
  }
}
