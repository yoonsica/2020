package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.annotation.TimeStatistic;
import com.example.entity.Employee;
import java.util.List;

/**
 * @Classname EmployeeService
 * @Description TODO
 * @Date 2021/5/21 下午2:14
 * @Author shengli
 */
public interface EmployeeService extends IService<Employee> {
    @TimeStatistic(text = "listAllByLastName")
    List<Employee> listAllByLastName(String lastName);
}
