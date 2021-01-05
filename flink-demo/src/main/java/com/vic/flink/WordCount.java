package com.vic.flink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * @Author ShengLi
 * 批处理
 */
public class WordCount {
    public static void main(String[] args) throws Exception {
        //创建执行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        //从文件中读取数据
        String inputPath = "/Users/shengli/IdeaProjects/2020/flink-demo/src/main/resources/hello.txt";
        DataSet<String> inputDataSet = env.readTextFile(inputPath);
        DataSet<Tuple2<String,Integer>> resultDs = inputDataSet.flatMap(new FlatMapFunction<String, Tuple2<String,Integer>>() {

            @Override
            public void flatMap(String s, Collector<Tuple2<String,Integer>> collector) throws Exception {
                String[] words = s.split(" ");
                for (String word : words) {
                    collector.collect(new Tuple2<>(word, 1));
                }
            }
        }).groupBy(0).sum(1);
        resultDs.print();
    }
}
