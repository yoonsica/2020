package com.example.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.annotation.TimeStatistic;
import com.example.entity.Employee;
import com.example.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Classname EmployeeServiceImpl
 * @Description TODO
 * @Date 2021/5/21 下午2:16
 * @Author shengli
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
        implements EmployeeService {

    @TimeStatistic(text = "listAllByLastName")
    @Override
    public List<Employee> listAllByLastName(String lastName) {
        return baseMapper.selectAllByLastName(lastName);
    }
}