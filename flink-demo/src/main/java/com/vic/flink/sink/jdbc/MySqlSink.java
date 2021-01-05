package com.vic.flink.sink.jdbc;

import com.vic.flink.beans.SensorReading;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * @Classname MySqlSink
 * @Description TODO
 * @Date 2021/1/3 上午11:32
 * @Author shengli
 */
public class MySqlSink extends RichSinkFunction<SensorReading> {
    String url;
    String name;
    String password;
    Connection connection;
    PreparedStatement updateSt;
    PreparedStatement insertSt;
    public MySqlSink() {
    }

    public MySqlSink(String url, String name, String password) {
        this.url = url;
        this.name = name;
        this.password = password;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        connection = DriverManager.getConnection(url, name, password);
        insertSt = connection.prepareStatement("insert into sensor_temp (id,temp) values (?,?)");
        updateSt = connection.prepareStatement("update sensor_temp set temp = ? where id = ?");
    }

    /**
     * Writes the given value to the sink. This function is called for every record.
     *
     * <p>You have to override this method when implementing a {@code SinkFunction}, this is a
     * {@code default} method for backward compatibility with the old-style method only.
     *
     * @param value   The input record.
     * @param context Additional context about the input record.
     * @throws Exception This method may throw exceptions. Throwing an exception will cause the operation
     *                   to fail and may trigger recovery.
     */
    @Override
    public void invoke(SensorReading value, Context context) throws Exception {
        updateSt.setDouble(1, value.getTemperature());
        updateSt.setString(2, value.getId());
        updateSt.execute();
        if (updateSt.getUpdateCount() == 0) {
            insertSt.setDouble(2, value.getTemperature());
            insertSt.setString(1,value.getId());
            insertSt.execute();
        }
    }

    @Override
    public void close() throws Exception {
        insertSt.close();
        updateSt.close();
        connection.close();
    }
}
