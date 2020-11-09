package com.wondero.atmredo;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HP
 * 类数据库操纵类
 */
public class DataBase {
    /**
     * 读取文件
     */
    public static Map<String, Account> reader() {
        Map<String, Account> userData = new HashMap<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("accounts.txt"))) {
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                //将文件的每一行数据用逗号分割并存入col数组
                String[] col = s.split(",");
                Account account = new Account(col[0], col[1], Integer.parseInt(col[2]), Integer.parseInt(col[3]));
                userData.put(account.getId(), account);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userData;
    }

    /**
     * 写入文件
     */
    public static void writer(Map<String, Account> userData) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("accounts.txt"))) {
            for (Map.Entry<String, Account> entry : userData.entrySet()) {
                bufferedWriter.write(entry.getValue().getId() + "," + entry.getValue().getPassword() + "," + entry.getValue().getBalance() + "," + entry.getValue().getFrozen() + "\n");
            }
            //刷新流并强制将他们写入到文件
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
