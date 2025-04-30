package com.myappiumproject.data;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.List;
import java.util.Properties;

public class EmailSender {
    private final String username;  // 163邮箱账号
    private final String password;  // 授权码(不是邮箱密码)
    private final Properties props;

    public EmailSender(String username, String password) {
        this.username = username;
        this.password = password;

        // 配置邮件服务器
        this.props = new Properties();
        props.put("mail.smtp.host", "smtp.163.com"); // SMTP服务器地址
        props.put("mail.smtp.port", "465"); // 端口
        props.put("mail.smtp.auth", "true"); // 需要认证
        props.put("mail.smtp.ssl.enable", "true"); // 启用SSL
        props.put("mail.smtp.ssl.protocols", "TLSv1.2"); // 协议版本
        props.put("mail.debug", "true"); // 调试模式(生产环境应关闭)
    }

    public void sendAlertEmail(List<String> toEmails, String subject, String content) throws MessagingException {
        // 创建会话
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        // 创建邮件
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));

        // 关键修改：设置多个收件人
        InternetAddress[] addresses = toEmails.stream()
                        .map(email -> {
                            try {
                                return new InternetAddress(email);
                            } catch (AddressException e) {
                                throw new RuntimeException("无效邮箱地址: " + email, e);
                            }
                        })
                        .toArray(InternetAddress[]::new);

        message.setRecipients(Message.RecipientType.TO, addresses);

        message.setSubject(subject, "UTF-8");

        // HTML格式内容
        String htmlContent = "<html><body>" +
                "<h2 style='color:red'>API监控告警</h2>" +
                "<pre>" + content + "</pre>" +
                "<p>时间: " + System.currentTimeMillis() + "</p>" +
                "</body></html>";

        message.setContent(htmlContent, "text/html;charset=UTF-8");

        // 发送邮件
        Transport.send(message);
    }
}