package com.helecloud.streams.demo.processing;

import static java.util.Objects.isNull;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.springframework.stereotype.Component;

import com.helecloud.streams.demo.model.Vehicle;
import com.helecloud.streams.demo.model.VehicleStatistics;

@Component
public class VehicleStatisticsAggregator implements AggregateFunction<Vehicle, VehicleStatistics, VehicleStatistics> {

  @Override
  public VehicleStatistics createAccumulator() {
    return new VehicleStatistics();
  }

  @Override
  public VehicleStatistics add(Vehicle vehicle, VehicleStatistics vehicleStatistics) {

    if(isNull(vehicleStatistics.getVehicleType())) {
      vehicleStatistics.setVehicleType(vehicle.getVehicleType());
    }

    if(isNull(vehicleStatistics.getStart())) {

      vehicleStatistics.setStart(vehicle.getTimestamp());
    }

    if(isNull(vehicleStatistics.getCount())) {

      vehicleStatistics.setCount(1);
    } else {

      vehicleStatistics.setCount(vehicleStatistics.getCount() + 1);
    }

    vehicleStatistics.setEnd(vehicle.getTimestamp());

    return vehicleStatistics;
  }

  @Override
  public VehicleStatistics getResult(VehicleStatistics vehicleStatistics) {
    return vehicleStatistics;
  }

  @Override
  public VehicleStatistics merge(VehicleStatistics vehicleStatistics, VehicleStatistics accumulator) {

    return new VehicleStatistics(
        vehicleStatistics.getStart(),
        accumulator.getEnd(),
        vehicleStatistics.getVehicleType(),
        vehicleStatistics.getCount() + accumulator.getCount());

  }
}
