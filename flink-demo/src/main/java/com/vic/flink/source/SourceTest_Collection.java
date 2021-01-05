package com.vic.flink.source;

import com.vic.flink.beans.SensorReading;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Arrays;

/**
 * @Classname SourceTest_Collection
 * @Description TODO
 * @Date 2020/12/27 上午8:40
 * @Author shengli
 */
public class SourceTest_Collection {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<SensorReading> dataStream = env.fromCollection(Arrays.asList(
                new SensorReading("sensor_1", 1547718199L, 35.8),
                new SensorReading("sensor_2", 1547718200L, 15.4),
                new SensorReading("sensor_3", 1547718201L, 32.8),
                new SensorReading("sensor_4", 1547718202L, 34.8)
        ));
        DataStream<Integer> integerDataStream = env.fromElements(1, 2, 4, 67, 100);
        dataStream.print("data");
        integerDataStream.print("int");
        env.execute("sourceTest");
    }

}
