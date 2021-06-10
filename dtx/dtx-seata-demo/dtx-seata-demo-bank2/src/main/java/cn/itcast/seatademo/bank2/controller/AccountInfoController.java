package cn.itcast.seatademo.bank2.controller;

import cn.itcast.seatademo.bank2.service.AccountInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname AccountInfoController
 * @Description TODO
 * @Date 2021/2/4 下午5:09
 * @Author shengli
 */
@RestController
public class AccountInfoController {
    @Autowired
    AccountInfoService accountInfoService;

    @GetMapping("/transfer")
    public String transfer(Double amount) {
        accountInfoService.updateAccountBalance("2", amount);
        return "bank2" + amount;
    }
}
