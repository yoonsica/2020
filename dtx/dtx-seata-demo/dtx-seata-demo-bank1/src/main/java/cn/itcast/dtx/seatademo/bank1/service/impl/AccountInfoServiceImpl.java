package cn.itcast.dtx.seatademo.bank1.service.impl;

import cn.itcast.dtx.seatademo.bank1.dao.AccountInfoDao;
import cn.itcast.dtx.seatademo.bank1.service.AccountInfoService;
import cn.itcast.dtx.seatademo.bank1.spring.Bank2Client;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Classname AccountInfoServiceImpl
 * @Description TODO
 * @Date 2021/2/4 下午4:47
 * @Author shengli
 */
@Service
@Slf4j
public class AccountInfoServiceImpl implements AccountInfoService {
    @Autowired
    AccountInfoDao accountInfoDao;

    @Autowired
    Bank2Client bank2Client;

    @Override
    @GlobalTransactional
    @Transactional
    public void updateAccountBalance(String accountNo, Double amount) {
        log.info("******** Bank1 Service Begin ... xid: {}" , RootContext.getXID());
        accountInfoDao.updateAccountBalance(accountNo, amount * -1);
        String remote = bank2Client.transfer(amount);
        if (remote.equals("fallback")) {
            throw new RuntimeException("bank2 服务调用异常");
        }
        if (amount < 0) {
            throw new RuntimeException("bank1 服务异常");
        }
    }
}
