package com.example.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 属性自动填充处理
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 实现插入时的自动填充
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("insert开始属性填充");
        this.strictInsertFill(metaObject,"gmtCreate", LocalDateTime.class,LocalDateTime.now());
        this.strictInsertFill(metaObject,"gmtModified", LocalDateTime.class,LocalDateTime.now());
    }

    /**
     * 实现更新时的自动填充
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("update开始属性填充");
        this.strictInsertFill(metaObject,"gmtModified", LocalDateTime.class,LocalDateTime.now());
    }
}