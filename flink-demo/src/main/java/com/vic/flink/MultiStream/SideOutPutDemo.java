package com.vic.flink.MultiStream;

import com.vic.flink.beans.SensorReading;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

/**
 * @Classname SideOutPutDemo
 * @Description 利用侧输出流进行分流demo
 * @Date 2021/1/21 下午3:46
 * @Author shengli
 */
public class SideOutPutDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);//这里不加这个就不会按视频一样输出，原因还不明确
        DataStream<String> inputStream = env.socketTextStream("localhost", 7777);
        DataStream<SensorReading> sensorStream = inputStream.map(s -> {
            String[] a = s.split(",");
            return new SensorReading(a[0],new Long(a[1]),new Double(a[2]));
        });
        OutputTag<SensorReading> lowTempOutputTag = new OutputTag<SensorReading>("lowTemp"){};
        //自定义侧输出流，实现分流操作
        SingleOutputStreamOperator<SensorReading> highTempStream = sensorStream.process(new ProcessFunction<SensorReading, SensorReading>() {
            @Override
            public void processElement(SensorReading value, Context ctx, Collector<SensorReading> out) throws Exception {
                if (value.getTemperature() > 30) {
                    out.collect(value);
                } else {
                    ctx.output(lowTempOutputTag, value);
                }
            }
        });
        highTempStream.print("highTemp");
        highTempStream.getSideOutput(lowTempOutputTag).print("lowTemp");
        env.execute();
    }
}
