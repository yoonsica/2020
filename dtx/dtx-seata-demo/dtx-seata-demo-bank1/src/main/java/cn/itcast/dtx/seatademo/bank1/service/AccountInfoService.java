package cn.itcast.dtx.seatademo.bank1.service;

/**
 * @Classname AccountInfoService
 * @Description TODO
 * @Date 2021/2/4 下午4:45
 * @Author shengli
 */
public interface AccountInfoService {
    /**
     * 扣款接口
     * @param accountNo
     * @param amount
     */
    void updateAccountBalance(String accountNo, Double amount);
}
