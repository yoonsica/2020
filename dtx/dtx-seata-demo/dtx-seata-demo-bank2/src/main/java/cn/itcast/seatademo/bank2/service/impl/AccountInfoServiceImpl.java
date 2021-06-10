package cn.itcast.seatademo.bank2.service.impl;

import cn.itcast.seatademo.bank2.dao.AccountInfoDao;
import cn.itcast.seatademo.bank2.service.AccountInfoService;
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

    @Override
    @Transactional
    public void updateAccountBalance(String accountNo, Double amount) {
        log.info("******** Bank2 Service Begin ... xid: {}" , RootContext.getXID());
        accountInfoDao.updateAccountBalance(accountNo, amount);
        if (amount==3) {
            throw new RuntimeException("bank2 服务异常");
        }
    }
}
