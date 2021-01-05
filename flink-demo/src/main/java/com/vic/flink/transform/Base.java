package com.vic.flink.transform;

import com.vic.flink.beans.SensorReading;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import javax.xml.crypto.Data;

/**
 * @Classname Base
 * @Description TODO
 * @Date 2020/12/27 上午11:02
 * @Author shengli
 */
public class Base {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStream<String> inputStream = env.readTextFile("/Users/shengli/IdeaProjects/2020/flink-demo/src/main/resources/sensor.txt");

        //map
        DataStream<Tuple2<String,String>> mapStream = inputStream.map(line ->{
            return new Tuple2<>(line.split(",")[0],line.split(",")[2]);
        }).returns(Types.TUPLE(Types.STRING,Types.STRING));

        DataStream<Tuple2<String,Integer>> flatMapStream = inputStream.flatMap(new FlatMapFunction<String, Tuple2<String,Integer>>() {
            @Override
            public void flatMap(String s, Collector<Tuple2<String,Integer>> collector) throws Exception {
                for (String word : s.split(",")) {
                    collector.collect(new Tuple2<>(word, 1));
                }
            }
        });

        DataStream<String> filterMap = inputStream.filter(s -> s.startsWith("sensor_1"));

        DataStream<SensorReading> sensorStream = inputStream.map((MapFunction<String, SensorReading>) s -> {
            String[] a = s.split(",");
            return new SensorReading(a[0],new Long(a[1]),new Double(a[2]));
        });

        KeyedStream<SensorReading, Tuple> keyedStream = sensorStream.keyBy("id");
        DataStream<SensorReading> temperature = keyedStream.maxBy("temperature");

        //reduce 聚合，取最大温度值，以及当前最新的时间戳
        DataStream<SensorReading> reduceStream = keyedStream.reduce((ReduceFunction<SensorReading>) (t1, t2) ->
                new SensorReading(t1.getId(), t2.getTimestamp(), Math.max(t1.getTemperature(), t2.getTemperature())));
        mapStream.print("map");
        flatMapStream.print("flatmap");
        filterMap.print("filter");
        sensorStream.print("sensorStream");
        temperature.print("maxByTemperature");
        reduceStream.print("reduceStream");
        env.execute();

    }
}
