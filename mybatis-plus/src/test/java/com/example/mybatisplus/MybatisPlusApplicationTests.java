package com.example.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Employee;
import com.example.entity.Shop;
import com.example.mapper.EmployeeMapper;
import com.example.mapper.ShopMapper;
import com.example.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@MapperScan("com.example.mapper")
@ComponentScan("com.example.*")
class MybatisPlusApplicationTests {

//    @Autowired
//    private EmployeeMapper employeeMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private EmployeeService employeeService;

    @Test
    void contextLoads() {
        /*List<Employee> employeeList = employeeMapper.selectList(null);
        employeeList.forEach(System.out::println);*/
        /*List<Employee> employeeList = employeeService.list();
        employeeList.forEach(System.out::println);*/
        List<Employee> list = employeeService.listAllByLastName("tom");
        list.forEach(System.out::println);
    }

    @Test
    void testInsert() {
        Employee employee = new Employee();
        employee.setLastName("lisa");
        employee.setEmail("lisa@qq.com");
        employee.setAge(20);
        // 设置创建时间
        employee.setGmtCreate(LocalDateTime.now());
        employee.setGmtModified(LocalDateTime.now());
        employeeService.save(employee);
    }

    @Test
    void testUpdate(){
        Employee employee = new Employee();
        employee.setId(1395988278292824066L);
        employee.setAge(15);
        employeeService.updateById(employee);
    }

    /**
     * 分页查询测试
     */
    @Test
    void testPage(){
        /*Page<Employee> page = new Page<>(1,2);
        employeeService.page(page, null);
        List<Employee> employeeList = page.getRecords();
        employeeList.forEach(System.out::println);
        System.out.println("获取总条数:" + page.getTotal());
        System.out.println("获取当前页码:" + page.getCurrent());
        System.out.println("获取总页码:" + page.getPages());
        System.out.println("获取每页显示的数据条数:" + page.getSize());
        System.out.println("是否有上一页:" + page.hasPrevious());
        System.out.println("是否有下一页:" + page.hasNext());*/

        Page<Employee> page = new Page<>(1, 2);
        employeeService.page(page, new QueryWrapper<Employee>()
                .between("age", 20, 50)
                .eq("gender", 1));
        List<Employee> employeeList = page.getRecords();
        employeeList.forEach(System.out::println);
    }

    @Test
    void testOptimisticLock() {
        // A、B管理员读取数据
        Shop A = shopMapper.selectById(1L);
        Shop B = shopMapper.selectById(1L);
        // B管理员先修改
        B.setPrice(9000);
        int result = shopMapper.updateById(B);
        if (result == 1) {
            System.out.println("B管理员修改成功!");
        } else {
            System.out.println("B管理员修改失败!");
        }
        // A管理员后修改
        A.setPrice(8500);
        int result2 = shopMapper.updateById(A);
        if (result2 == 1) {
            System.out.println("A管理员修改成功!");
        } else {
            System.out.println("A管理员修改失败!");
        }
        // 最后查询
        System.out.println(shopMapper.selectById(1L));
    }

}
