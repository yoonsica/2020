package com.vic.flink.source;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;

import java.util.Properties;

/**
 * @Classname KafkaSourceDemo
 * @Description flink链接kafka demo
 * @Date 2020/12/24 下午5:00
 * @Author shengli
 */
public class KafkaSourceDemo {
    //启动kafka的生产者：kafka-console-producer.sh
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");

        //从kafka读取数据
        DataStream<String> dataStream = env.addSource(
                new FlinkKafkaConsumer011<String>("sensor", new SimpleStringSchema(), properties));
        dataStream.print();
        env.execute();
    }

}
