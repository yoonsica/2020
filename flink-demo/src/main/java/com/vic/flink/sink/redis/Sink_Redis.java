package com.vic.flink.sink.redis;

import com.vic.flink.beans.SensorReading;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.redis.RedisSink;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;

/**
 * @Classname Sink_Redis
 * @Description 存储数据到redis
 * @Date 2020/12/31 上午9:57
 * @Author shengli
 */
public class Sink_Redis {
    //方式一：使用brew帮助我们启动软件
    //brew services start redis
    //方式二
    //redis-server /usr/local/etc/redis.conf
    //redis-cli -h 127.0.0.1 -p 6379
    //redis-cli shutdown
    //sudo pkill redis-server
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStream<String> inputStream = env.readTextFile("/Users/shengli/IdeaProjects/2020/flink-demo/src/main/resources/sensor.txt");
        DataStream<SensorReading> sensorStream = inputStream.map((MapFunction<String, SensorReading>) s -> {
            String[] a = s.split(",");
            return new SensorReading(a[0],new Long(a[1]),new Double(a[2]));
        });

        //定义jedis连接配置
        FlinkJedisPoolConfig jedisPoolConfig = new FlinkJedisPoolConfig.Builder()
                .setHost("localhost")
                .setPort(6379)
                .build();

        sensorStream.addSink(new RedisSink<>(jedisPoolConfig, new RedisMapper<SensorReading>() {
            //定义保存redis命令 存hash表 hset sensor_temp id temperature
            @Override
            public RedisCommandDescription getCommandDescription() {
                return new RedisCommandDescription(RedisCommand.HSET, "sensor_temp");
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

        //hget sensor_temp sensor_1
        //hgetall sensor_temp
        env.execute();
    }
}
