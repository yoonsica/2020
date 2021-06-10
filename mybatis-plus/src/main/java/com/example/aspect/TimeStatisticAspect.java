package com.example.aspect;
import com.example.annotation.TimeStatistic;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalTime;


/**
 * @Classname TimeStatisticAsepect
 * @Description TODO
 * @Date 2021/5/24 上午10:11
 * @Author shengli
 */
@Aspect
@Component
@Order(1)
public class TimeStatisticAspect {

    /**
     * 切入点
     * 标识切入哪些方法
     */
    @Pointcut("@annotation(com.example.annotation.TimeStatistic)")
    public void timeStatistic(){}

    /**
     * 切面表达式
     * 修饰符 返回值 包名 方法名(参数) 异常
     */
    /*@Pointcut("execution(public * com.example.service.*.*(..))")
    public void timeStatistic1(){}*/

    /*@Before("timeStatistic()")
    public void before(JoinPoint joinPoint){
        //获取方法，此处可将signature强转为MethodSignature
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        System.out.println("方法名称："+method.getName());
        System.out.println("开始时间："+LocalTime.now());
        TimeStatistic annotation = method.getAnnotation(TimeStatistic.class);
        System.out.println("注解参数："+annotation.text());
    }*/

    @Around("timeStatistic()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        System.out.println("方法名称："+method.getName());
        LocalTime start = LocalTime.now();
        System.out.println("开始时间："+start);
        TimeStatistic annotation = method.getAnnotation(TimeStatistic.class);
        System.out.println("注解参数："+annotation.text());
        Object obj = joinPoint.proceed();
        LocalTime end = LocalTime.now();
        System.out.println("结束时间："+end);
        System.out.println("方法耗时："+ Duration.between(start,end).toMillis() +"ms");
        return obj;
    }
}
