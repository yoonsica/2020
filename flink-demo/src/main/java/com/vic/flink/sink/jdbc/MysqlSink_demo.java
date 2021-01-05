package com.vic.flink.sink.jdbc;

import com.vic.flink.beans.SensorReading;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @Classname MysqlSink
 * @Description TODO
 * @Date 2021/1/3 上午11:25
 * @Author shengli
 */
public class MysqlSink_demo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStream<String> inputStream = env.readTextFile("/Users/shengli/IdeaProjects/2020/flink-demo/src/main/resources/sensor.txt");
        DataStream<SensorReading> sensorStream = inputStream.map((MapFunction<String, SensorReading>) s -> {
            String[] a = s.split(",");
            return new SensorReading(a[0],new Long(a[1]),new Double(a[2]));
        });
        sensorStream.addSink(new MySqlSink());
        env.execute();
    }
}
