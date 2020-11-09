package com.wondero.atmredo;

import java.util.Scanner;

/**
 * @author 孙一博
 * 用户控制台交互界面
 */
public class ATMConloseInterface {

    Scanner in = new Scanner(System.in);
    BankServer bankServer = new BankServer();

    /**
     * 系统启动时加载的首页
     */
    public void index() {
        System.out.println("欢迎使用天地银行自动柜员机");
        System.out.println("输入卡号或插卡来开始");
        String userInputId = in.next();
        //剩余尝试次数
        int countsOfTry = 3;
        if (bankServer.isExsit(userInputId)) {
            if (bankServer.isFrozen(userInputId)) {
                System.out.println("你的账户已被冻结，去死吧");
            } else {
                while (countsOfTry >= 0) {
                    System.out.println("输入密码以继续：");
                    String userInputPassword = in.next();
                    if (bankServer.passwordVerifying(userInputId, userInputPassword)) {
                        System.out.println("欢迎回来，主人");
                        menu(userInputId);
                        index();
                    } else {
                        if (countsOfTry != 0) {
                            System.out.println("错误的密码，您还可以尝试" + countsOfTry + "次");
                        }
                        countsOfTry--;
                    }
                }
                System.out.println("这卡不是你的吧，冻结了~");
                bankServer.frozen(userInputId);
            }
        } else {
            System.out.println("卡号不正确或用户不存在");
        }
        index();
    }

    /**
     * 用户登录成功后加载的菜单页面
     */
    public void menu(String userInputId) {
        System.out.println("业务选择");
        System.out.println("1.查询\n2.存款\n3.取款\n4.转账\n5.退卡");
        switch (in.next()) {
            case "1":
                queryUI(userInputId);
                break;
            case "2":
                withdrawUI(userInputId);
                break;
            case "3":
                depositUI(userInputId);
                break;
            case "4":
                transferUI(userInputId);
            default:
                System.out.println("系统出现异常\n根据保护策略，即将关机");
                System.exit(0);
        }
    }

    public void queryUI(String userInputId) {
        System.out.println("账户" + userInputId + "当前余额为" + bankServer.queryBalance(userInputId));
        menu(userInputId);
    }

    public void withdrawUI(String userInputId) {
        System.out.println("你想取多少钱？");
        System.out.println("目前本机单次最多可以取10000元");
        int withdrawCashCache = in.nextInt();
        if (withdrawCashCache % 100 == 0 && withdrawCashCache <= 10000) {
            if (bankServer.queryBalance(userInputId) >= withdrawCashCache) {
                bankServer.withdrawCash(userInputId, withdrawCashCache);
                System.out.println("如果我没猜错，你的现金应该会出现在下面的洞洞里。");
                System.out.println("输入1打印回执，输入其他按键跳过这一步");
                if (in.next().equals("1")) {
                    String receiptContent = "账户" + userInputId + "存了" + withdrawCashCache + "元";
                    bankServer.receiptPrinter(receiptContent);
                }
            } else {
                System.out.println("没钱你还来？滚吧");
            }
        } else {
            System.out.println("我没有零钱啦");
        }
    }

    public void depositUI(String userInputId) {
        System.out.println("你想存多少钱？\n注意我只能识别百元大钞哦");
        int depositCashCache = in.nextInt();
        if (depositCashCache % 100 == 0) {
            System.out.println("请将现金平整放置于下方入钞口，就绪后请按1");
            if (in.next().equals("1")) {
                bankServer.depositCash(userInputId, depositCashCache);
                System.out.println("存款完成\n输入1打印回执，输入其他按键跳过这一步");
                if (in.next().equals("1")) {
                    String receiptContent = "账户" + userInputId + "存了" + depositCashCache + "元";
                    bankServer.receiptPrinter(receiptContent);
                }
            } else {
                System.exit(0);
            }
        } else {
            System.out.println("都说了我只能识别百元大钞！");
        }
    }

    public void transferUI(String userInputId) {
        System.out.println("输入目标账户");
        String destinationId = in.next();
        if (bankServer.isExsit(destinationId) && !bankServer.isFrozen(destinationId) && !destinationId.equals(userInputId)) {
            System.out.println("你想转多少钱给他呢？");
            int transferCashCache = in.nextInt();
            if (bankServer.queryBalance(userInputId) >= transferCashCache) {
                bankServer.transferToUser(userInputId, destinationId, transferCashCache);
                System.out.println("转账成功，请联系对方在数分钟后查看他的账户余额，到账时间取决于您的颜值。\n输入1打印回执，输入其他按键跳过这一步");
                if (in.next().equals("1")) {
                    String receiptContent = "账户" + userInputId + "向账户" + destinationId + "转了" + transferCashCache + "元";
                    bankServer.receiptPrinter(receiptContent);
                }
            } else {
                System.out.println("没钱你还转个锤子，滚吧");
            }
        } else {
            System.out.println("账户不存在或者转给了自己。");
        }
    }

}
