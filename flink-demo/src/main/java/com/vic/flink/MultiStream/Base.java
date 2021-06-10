package com.vic.flink.MultiStream;

import com.vic.flink.beans.SensorReading;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.collector.selector.OutputSelector;
import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.SplitStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;

import java.util.Collections;

/**
 * @Classname Base
 * @Description 多条流处理
 * @Date 2020/12/28 下午5:04
 * @Author shengli
 */
public class Base {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStream<String> inputStream = env.readTextFile("/Users/shengli/IdeaProjects/2020/flink-demo/src/main/resources/sensor.txt");
        DataStream<SensorReading> sensorStream = inputStream.map((MapFunction<String, SensorReading>) s -> {
            String[] a = s.split(",");
            return new SensorReading(a[0],new Long(a[1]),new Double(a[2]));
        });

        //分流
        SplitStream<SensorReading> splitStream = sensorStream.split((OutputSelector<SensorReading>) value -> value.getTemperature() > 30 ?
                Collections.singletonList("high") : Collections.singleton("low"));


        DataStream<SensorReading> highStream = splitStream.select("high");
        highStream.print("high");
        DataStream<SensorReading> lowStream = splitStream.select("low");
        lowStream.print("low");

        DataStream<Tuple2<String, Double>> highTempStream = highStream.map((MapFunction<SensorReading, Tuple2<String, Double>>) line -> new Tuple2<>(line.getId(), line.getTemperature()))
                .returns(Types.TUPLE(Types.STRING,Types.DOUBLE));

        ConnectedStreams<SensorReading, Tuple2<String, Double>> connectStream = lowStream.connect(highTempStream);
        connectStream.map(new CoMapFunction<SensorReading, Tuple2<String, Double>, Object>() {
            /**
             * This method is called for each element in the first of the connected streams.
             *
             * @param value The stream element
             * @return The resulting element
             * @throws Exception The function may throw exceptions which cause the streaming program
             *                   to fail and go into recovery.
             */
            @Override
            public Object map1(SensorReading value) throws Exception {
                return new Tuple3<>(value.getId(),value.getTimestamp(),"low stream");
            }

            /**
             * This method is called for each element in the second of the connected streams.
             *
             * @param value The stream element
             * @return The resulting element
             * @throws Exception The function may throw exceptions which cause the streaming program
             *                   to fail and go into recovery.
             */
            @Override
            public Object map2(Tuple2<String, Double> value) throws Exception {
                return new Tuple3<>(value.f0,value.f1,"high temp");
            }
        }).print("resultStream");

        //union可以联合多条流，数据类型必须一样 connect只能连接两条流，数据类型可不同，通过comapfunction分别处理
        highStream.union(lowStream, sensorStream).print("union");
        env.execute();
    }
}
