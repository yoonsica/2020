package cn.itcast.dtx.seatademo.bank1.spring;

import org.springframework.stereotype.Component;

/**
 * @Classname Bank2ClientFallback
 * @Description TODO
 * @Date 2021/2/4 下午5:02
 * @Author shengli
 */
@Component
public class Bank2ClientFallback implements Bank2Client{

    @Override
    public String transfer(Double amount) {
        return "fallback";
    }

}
