package com.vic.flink.processfunction;

import com.vic.flink.beans.SensorReading;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

/**
 * @Classname KeyedProcessFunction_Test
 * @Description TODO
 * @Date 2021/1/18 上午9:21
 * @Author shengli
 */
public class KeyedProcessFunction_Test {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStream<String> inputStream = env.socketTextStream("localhost", 7777);
        DataStream<SensorReading> dataStream = inputStream.map(line -> {
            String[] fields = line.split(",");
            return new SensorReading(fields[0], new Long(fields[1]), new Double(fields[2]));
        });

        //测试KeyedProcessFunction
        dataStream.keyBy("id").process(new TempIncreaseTendWarning(5)).print();

        env.execute();
    }

    /*public static class MyProcess extends KeyedProcessFunction<Tuple, SensorReading, Integer> {

        @Override
        public void processElement(SensorReading value, Context ctx, Collector<Integer> out) throws Exception {
            out.collect(value.getId().length());

        }
    }*/
    public static class TempIncreaseTendWarning extends KeyedProcessFunction<Tuple, SensorReading, String> {

        private int interval;
        private ValueState<Double> lastTempSt;
        private ValueState<Long> timerSt;
        public TempIncreaseTendWarning(int interval) {
            this.interval = interval;
        }

        @Override
        public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
            out.collect(ctx.getCurrentKey().getField(0) + " 连续" + interval + "s温度上升，最新温度值："
                    + lastTempSt.value());
            timerSt.clear();
            lastTempSt.clear();
        }

        @Override
        public void open(Configuration parameters) throws Exception {
            lastTempSt = getRuntimeContext().getState(new ValueStateDescriptor<Double>("lastTemp", Double.class));
            timerSt = getRuntimeContext().getState(new ValueStateDescriptor<Long>("timer", Long.class));
        }

        @Override
        public void close() throws Exception {
            lastTempSt.clear();
            timerSt.clear();
        }

        @Override
        public void processElement(SensorReading value, Context ctx, Collector<String> out) throws Exception {
            Double lastTemp = lastTempSt.value();
            Long timer = timerSt.value();
            if (timer == null) {
                //如果没有定时器,且最新温度不是空，则新建一个定时器
                if (lastTemp != null && value.getTemperature() > lastTemp) {
                    Long ts = ctx.timerService().currentProcessingTime() + interval * 1000L;
                    ctx.timerService().registerProcessingTimeTimer(ts);
                    timerSt.update(ts);
                }
                lastTempSt.update(value.getTemperature());
            } else {
                //如果定时器不为空，则需要判断温度是否上升
                if (value.getTemperature() < lastTemp) {
                    //温度下降,删除定时器
                    ctx.timerService().deleteProcessingTimeTimer(timer);
                    timerSt.clear();
                    lastTempSt.clear();
                } else {
                    lastTempSt.update(value.getTemperature());
                }
            }
        }
    }
}
