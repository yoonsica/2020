package com.vic.flink.state;

import com.vic.flink.beans.SensorReading;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @Classname Application
 * @Description 计算与最近一次温度差，大于5度则报警
 * @Date 2021/1/9 下午11:58
 * @Author shengli
 */
public class Application {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);//这里不加这个就不会按视频一样输出，原因还不明确
        DataStream<String> inputStream = env.socketTextStream("localhost", 7777);
        DataStream<SensorReading> sensorStream = inputStream.map(s -> {
            String[] a = s.split(",");
            return new SensorReading(a[0],new Long(a[1]),new Double(a[2]));
        });
        //定义一个有状态的map操作操作，统计当前sensor数据个数
        sensorStream.keyBy("id").flatMap(new TempWarningFlatMapFunction(5.0)).print();
        env.execute();
    }

    private static class TempWarningFlatMapFunction extends RichFlatMapFunction<SensorReading,Tuple3<String,Double,Double>>{
        private Double threshold;//阈值
        private ValueState<Double> lastTempVS;

        public TempWarningFlatMapFunction(Double threshold) {
            this.threshold = threshold;
        }

        @Override
        public void open(Configuration parameters) throws Exception {
            lastTempVS = getRuntimeContext().getState(
                    new ValueStateDescriptor<Double>("key-count", Double.class));
        }

        @Override
        public void close() throws Exception {
            lastTempVS.clear();
        }

        @Override
        public void flatMap(SensorReading sensorReading, Collector<Tuple3<String, Double, Double>> collector) throws Exception {
            Double lastTemp = lastTempVS.value();
            if (lastTemp != null) {
                double diff = Math.abs(sensorReading.getTemperature() - lastTemp);
                if (diff >= threshold) {
                    collector.collect(new Tuple3<>(sensorReading.getId(),sensorReading.getTemperature(),lastTemp));
                }
            }
            lastTempVS.update(sensorReading.getTemperature());
        }
    }
}
