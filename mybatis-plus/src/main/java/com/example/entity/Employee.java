package com.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Classname Employee
 * @Description TODO
 * @Date 2021/5/21 上午10:32
 * @Author shengli
 */
@Data
@TableName("tbl_employee")
public class Employee {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String lastName;
    private String email;
    private Integer age;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;//当前数据创建的时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;//保存的是更新时间

    /**
     * 逻辑删除属性
     */
    @TableLogic
    @TableField("is_deleted")
    private Boolean deleted;
}
