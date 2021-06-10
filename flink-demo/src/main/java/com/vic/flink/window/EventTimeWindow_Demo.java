package com.vic.flink.window;

import com.vic.flink.beans.SensorReading;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.redis.RedisSink;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;

/**
 * @Classname EventTimeWindow_Demo
 * @Description TODO
 * @Date 2021/1/6 下午3:46
 * @Author shengli
 */
public class EventTimeWindow_Demo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //引入事件时间窗口
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        //env.setParallelism(1);//这里不加这个就不会按视频一样输出，原因还不明确
        env.getConfig().setAutoWatermarkInterval(100);
        DataStream<String> inputStream = env.socketTextStream("localhost", 7777);
        DataStream<SensorReading> sensorStream = inputStream.map(s -> {
            String[] a = s.split(",");
            return new SensorReading(a[0],new Long(a[1]),new Double(a[2]));
        }).assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<SensorReading>(Time.seconds(2)) {
            @Override
            public long extractTimestamp(SensorReading element) {
                return element.getTimestamp() * 1000L;
            }
        });
        //.setParallelism(1)这里设置就可以输出

        //统计15s内温度的最小值
        SingleOutputStreamOperator<SensorReading> minTemp = sensorStream.keyBy("id")
                        .timeWindow(Time.seconds(15))
                        .minBy("temperature");
        minTemp.print("minTemp");

        //定义jedis连接配置
        FlinkJedisPoolConfig jedisPoolConfig = new FlinkJedisPoolConfig.Builder()
                .setHost("localhost")
                .setPort(6379)
                .build();

        minTemp.addSink(new RedisSink<>(jedisPoolConfig, new RedisMapper<SensorReading>() {
            //定义保存redis命令 存hash表 hset minTemp id temperature
            @Override
            public RedisCommandDescription getCommandDescription() {
                return new RedisCommandDescription(RedisCommand.HSET, "minTemp");
            }

            @Override
            public String getKeyFromData(SensorReading sensorReading) {
                return sensorReading.getId();
            }

            @Override
            public String getValueFromData(SensorReading sensorReading) {
                return String.valueOf(sensorReading.getTemperature());
            }
        }));
        env.execute();
    }
}
