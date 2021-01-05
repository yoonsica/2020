package com.vic.flink.source;

import com.vic.flink.beans.SensorReading;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.HashMap;
import java.util.Random;

/**
 * @Classname UDF
 * @Description 自定义数据源,可用于开发测试时模拟生产数据源
 * @Date 2020/12/27 上午8:32
 * @Author shengli
 */
public class UDF {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStreamSource<SensorReading> dataStream = env.addSource(new MySensorSource());
        dataStream.print();
        env.execute();
    }

    private static class MySensorSource implements SourceFunction<SensorReading> {
        private boolean running = true;
        @Override
        public void run(SourceContext<SensorReading> sourceContext) throws Exception {
            Random random = new Random();
            HashMap<String, Double> sensorTempMap = new HashMap<>();
            for (int i = 0; i < 10; i++) {
                sensorTempMap.put("sensor_" + (i + 1), 60 + random.nextGaussian() * 20);
            }
            while (running) {
                for (String sensorId : sensorTempMap.keySet()) {
                    Double temp = sensorTempMap.get(sensorId) + random.nextGaussian();
                    sensorTempMap.put(sensorId, temp);
                    sourceContext.collect(new SensorReading(sensorId, System.currentTimeMillis(), temp));
                }
                Thread.sleep(1000);
            }
        }

        @Override
        public void cancel() {
            running = false;
        }
    }
}
