package com.thuwsy.xuetang.checkcode.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * ClassName: MailUtil
 * Package: com.thuwsy.xuetang.checkcode.utils
 * Description: 发送邮件工具类，主要用于邮箱验证码验证
 *
 * @Author THU_wsy
 * @Create 2024/2/4 12:36
 * @Version 1.0
 */
@Component
public class MailUtil {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String mailUser;

    /**
     * 发送邮件
     * @param email 收件邮箱号
     * @param code  验证码
     */
    public void sendMail(String email, String code){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailUser); // 发送者
        message.setTo(email); // 收件人
        message.setSubject("学堂在线邮件验证码"); // 邮件主题
        message.setText("尊敬的用户:你好!\n验证码为:" + code + "(有效期为一分钟,请勿告知他人)"); // 邮件内容
        javaMailSender.send(message); // 发送邮件
    }

    /**
     * 生成验证码
     */
    public static String achieveCode() {  //由于数字1、0和字母O、l有时分不清楚，所以没有数字1、0
        String[] beforeShuffle = new String[]{"2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F",
                "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a",
                "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
                "w", "x", "y", "z"};
        List<String> list = Arrays.asList(beforeShuffle); // 将数组转换为集合
        Collections.shuffle(list);  // 打乱集合顺序
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s); // 将集合转化为字符串
        }
        return sb.substring(3, 8);
    }
}
