package com.vic.flink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @Classname StreamWordCount
 * @Description 流式处理 wordcount
 * @Date 2020/12/22 上午8:50
 * @Author shengli
 */
public class StreamWordCount {
    //命令行提交任务命令 standalone 模式：flink run -c com.vic.flink.StreamWordCount -p 并行度 jar包绝对路径 --host localhost --port 7777等参数
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment sEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        //从文件中读取数据
        /*String inputPath = "/Users/shengli/IdeaProjects/2020/flink-demo/src/main/resources/hello.txt";
        DataStream<String> inputStream = sEnv.readTextFile(inputPath);*/

        //从启动参数中提取配置项
        ParameterTool pt = ParameterTool.fromArgs(args);
        String host = pt.get("host");
        int port = pt.getInt("port");
        //从socket文本流里读取数据
        DataStream<String> inputStream = sEnv.socketTextStream(host, port);

        DataStream<Tuple2<String, Integer>> resDs
                = inputStream.flatMap(new FlatMapFunction<String, Tuple2<String,Integer>>() {
            @Override
            public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {
                String[] words = s.split(" ");
                for (String word : words) {
                    collector.collect(new Tuple2<>(word, 1));
                }
            }
        }).keyBy(0).sum(1);
        resDs.print();
        sEnv.execute();
    }
}
