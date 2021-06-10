package com.vic.flink.state;

import com.vic.flink.beans.SensorReading;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @Classname KeyedStateDemo
 * @Description TODO
 * @Date 2021/1/7 下午4:17
 * @Author shengli
 */
public class KeyedStateDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);//这里不加这个就不会按视频一样输出，原因还不明确
        DataStream<String> inputStream = env.socketTextStream("localhost", 7777);
        DataStream<SensorReading> sensorStream = inputStream.map(s -> {
            String[] a = s.split(",");
            return new SensorReading(a[0],new Long(a[1]),new Double(a[2]));
        });
        //定义一个有状态的map操作操作，统计当前sensor数据个数
        sensorStream.keyBy("id").map(new MyKeyCountMapper()).print();
        env.execute();
    }

    private static class MyKeyCountMapper extends RichMapFunction<SensorReading, String> {

        private ValueState<Integer> keyCountState;

        @Override
        public void open(Configuration parameters) throws Exception {
            keyCountState = getRuntimeContext().getState(
                    new ValueStateDescriptor<Integer>("key-count", Integer.class,0));
        }

        @Override
        public String map(SensorReading sensorReading) throws Exception {
            Integer count = keyCountState.value() + 1;
            keyCountState.update(count);
            return sensorReading.getId()+":"+count;
        }
    }
}
