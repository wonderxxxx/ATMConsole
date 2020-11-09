package com.wondero.atmredo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * @author 本地的银行业务处理过程
 */
public class BankServer {
    private  Map<String, Account>  userData;

    public BankServer() {
        userData = DataBase.reader();
    }
//校验过程开始

    /**
     * 检查账户是否存在
     *
     * @param id
     * @return boolean
     */
    public  boolean isExsit(String id) {
        //检查是否包含，包含就返回true
        return userData.containsKey(id);
    }

    /**
     * 密码校验，将userdata中获取到的密码和传入的密码比对，比对成功即密码正确
     *
     * @param id
     * @param password
     * @return boolean
     */
    public  boolean passwordVerifying(String id, String password) {
        return userData.get(id).getPassword().equals(password);
    }

    /**
     * 检查账户冻结状态
     * 1为冻结
     *
     * @param id
     * @return boolean
     */
    public  boolean isFrozen(String id) {
        return userData.get(id).getFrozen() == 1;
    }

    //校验过程结束

    //业务过程开始

    /**
     * 余额查询
     *
     * @param id
     * @return int类型的账户余额
     */
    public  int queryBalance(String id) {
        return userData.get(id).getBalance();
    }

    /**
     * 存款，无返回值，存入的余额是查询到的余额加上本次传入的cash
     *
     * @param id
     * @param cash
     * @return
     */
    public  void depositCash(String id, int cash) {
        userData.get(id).setBalance(userData.get(id).getBalance() + cash);
        DataBase.writer(userData);
    }

    /**
     * 取款，无返回值，取出后的余额是查询到的余额减去本次取出的cash
     *
     * @param id
     * @param cash
     * @return
     */
    public  void withdrawCash(String id, int cash) {
        userData.get(id).setBalance(userData.get(id).getBalance() - cash);
        DataBase.writer(userData);
    }

    /**
     * @param originationId
     * @param destinationId
     * @param cash
     */
    public  void transferToUser(String originationId, String destinationId, int cash) {
        userData.get(originationId).setBalance(userData.get(originationId).getBalance() + cash);
        userData.get(destinationId).setBalance(userData.get(destinationId).getBalance() - cash);
        DataBase.writer(userData);
    }

    public   void frozen(String id) {
        userData.get(id).setFrozen(1);
        DataBase.writer(userData);
    }

    //业务过程结束

    /**
     * 回执打印器
     * @param receiptContent
     */
    public   void receiptPrinter(String receiptContent) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("receipt" + simpleDateFormat.format(date) + "txt"))) {
            bufferedWriter.write(receiptContent + "\n交易发生时间" + simpleDateFormat.format(date) + "\n为响应环保，我们将从近期停止提供纸质回执单\n关爱地球，从我做起\n【广告】\n澳门首家线上赌场上线啦，性感荷官在线发牌\n认准唯一官网：www.baidu.com");
            bufferedWriter.flush();
            System.out.println("回执已打印，如果没有打印成功请拨打客服热线妖妖灵，我们将热情为你服务。");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
