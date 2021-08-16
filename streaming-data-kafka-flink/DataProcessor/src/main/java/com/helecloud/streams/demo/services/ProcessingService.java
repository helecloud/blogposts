package com.helecloud.streams.demo.services;

import static com.helecloud.streams.demo.model.VehicleType.CAR;
import static com.helecloud.streams.demo.model.VehicleType.TRUCK;

import java.time.Duration;
import java.util.Properties;
import javax.annotation.PostConstruct;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.helecloud.streams.demo.model.Vehicle;
import com.helecloud.streams.demo.model.VehicleStatistics;
import com.helecloud.streams.demo.processing.VehicleStatisticsAggregator;
import com.helecloud.streams.demo.serialization.VehicleDeserializationSchema;
import com.helecloud.streams.demo.serialization.VehicleStatisticsSerializationSchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProcessingService {

  @Value("${kafka.bootstrap-servers}")
  private String kafkaAddress;

  @Value("${kafka.group-id}")
  private String kafkaGroupId;

  public static final String TOPIC = "vehicle-topic";

  public static final String VEHICLE_STATISTICS_TOPIC = "vehicle-statistics-topic";

  private final VehicleDeserializationSchema vehicleDeserializationSchema;

  private final VehicleStatisticsSerializationSchema vehicleStatisticsSerializationSchema;

  @PostConstruct
  public void startFlinkStreamProcessing() {
    try {

      processVehicleStatistic();
    } catch (Exception e) {

      log.error("Cannot process", e);
    }
  }

  public void processVehicleStatistic() throws Exception {

    StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();

    FlinkKafkaConsumer<Vehicle> consumer = createVehicleConsumerForTopic(TOPIC, kafkaAddress, kafkaGroupId);

    consumer.setStartFromLatest();

    consumer.assignTimestampsAndWatermarks(WatermarkStrategy.forMonotonousTimestamps());

    FlinkKafkaProducer<VehicleStatistics> producer = createVehicleStatisticsProducer(VEHICLE_STATISTICS_TOPIC, kafkaAddress);

    DataStream<Vehicle> inputMessagesStream = environment.addSource(consumer);

    inputMessagesStream
        .keyBy((vehicle -> vehicle.getVehicleType().ordinal()))
//        .filter(v -> CAR.equals(v.getVehicleType()))
        .window(TumblingEventTimeWindows.of(Time.seconds(20)))
//        .windowAll(TumblingEventTimeWindows.of(Time.seconds(10)))
        .aggregate(new VehicleStatisticsAggregator())
        .print();
//    .addSink(producer);

    environment.execute();

  }

  private FlinkKafkaConsumer<Vehicle> createVehicleConsumerForTopic(String topic, String kafkaAddress, String kafkaGroup ) {

    Properties properties = new Properties();

    properties.setProperty("bootstrap.servers", kafkaAddress);

    properties.setProperty("group.id", kafkaGroup);

    return new FlinkKafkaConsumer<>(topic, vehicleDeserializationSchema, properties);

  }

  private FlinkKafkaProducer<VehicleStatistics> createVehicleStatisticsProducer(String topic, String kafkaAddress){

    return new FlinkKafkaProducer<>(kafkaAddress, topic, vehicleStatisticsSerializationSchema);
  }

}
