package com.vic.flink.window;

import org.apache.commons.collections.IteratorUtils;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.Date;

/**
 * @Classname TimeWindow_Demo
 * @Description 时间窗口
 * @Date 2021/1/5 下午9:04
 * @Author shengli
 */
public class TimeWindow_Demo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<String> inputStream = env.socketTextStream("localhost", 7777);
        //增量聚合函数
        /*SingleOutputStreamOperator<String> aggregateStream = inputStream.keyBy((KeySelector<String, String>) s -> s.split(",")[0])
                .timeWindow(Time.seconds(15))
                .aggregate(new AggregateFunction<String, Integer, String>() {
                    @Override
                    public Integer createAccumulator() {
                        return 0;
                    }

                    @Override
                    public Integer add(String s, Integer integer) {
                        return Integer.valueOf(s.split(",")[1]) + integer;
                    }

                    @Override
                    public String getResult(Integer integer) {
                        return "共接收" + integer + "个";
                    }

                    @Override
                    public Integer merge(Integer integer, Integer acc1) {
                        return integer + acc1;
                    }
                });

        aggregateStream.print();*/
        //全窗口函数
        SingleOutputStreamOperator<Tuple3<String, String, Integer>> applyStream = inputStream.keyBy((KeySelector<String, String>) s -> s.split(",")[0])
                .timeWindow(Time.seconds(10))
                .apply(new WindowFunction<String, Tuple3<String, String, Integer>, String, TimeWindow>() {
                    public void apply(String s, TimeWindow window, Iterable<String> input,
                                      Collector<Tuple3<String, String, Integer>> out) throws Exception {
                        out.collect(new Tuple3<>(s, new Date(window.getEnd()).toString().toString(), IteratorUtils.toList(input.iterator()).size()));
                    }
                });
        applyStream.print();
        env.execute();

    }
}
