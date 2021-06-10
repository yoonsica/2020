package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Classname EmployeeMapper
 * @Description TODO
 * @Date 2021/5/21 上午10:33
 * @Author shengli
 */
public interface EmployeeMapper extends BaseMapper<Employee> {
    List<Employee> selectAllByLastName(@Param("lastName") String lastName);
}
