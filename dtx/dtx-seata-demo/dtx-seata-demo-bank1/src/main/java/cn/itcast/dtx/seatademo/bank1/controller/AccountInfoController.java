package cn.itcast.dtx.seatademo.bank1.controller;

import cn.itcast.dtx.seatademo.bank1.service.AccountInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
        accountInfoService.updateAccountBalance("1", amount);
        return "bank1" + amount;
    }
}
