package com.puyixiaowo.fblog.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.mail.util.MailSSLSocketFactory;
import org.apache.commons.lang3.StringUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;

/**
 * @author Moses
 * @date 2017-11-01 10:53
 */
public class ExceptionEmailUtils {

    private static final String USERNAME = "feihongblog@163.com";
    private static final String PASSWORD = "fblog207";//163授权码
    private static final String HOST = "smtp.163.com";
    private static final String PORT = "465";

    private static final String[] PACKAGE_LAMIC = {"com.puyixiaowo"};

    private static String EMAIL_ADDRESS_EXCEPTION = "ysdxz207@qq.com";


    public static void sendException(String title,
                                     Throwable ex) throws Exception {
        if (StringUtils.isEmpty(EMAIL_ADDRESS_EXCEPTION)) {
            EMAIL_ADDRESS_EXCEPTION = USERNAME;
        }
        String [] to;
        if (EMAIL_ADDRESS_EXCEPTION.indexOf(",") != -1) {
            to = EMAIL_ADDRESS_EXCEPTION.split(",");
        } else {
            to = EMAIL_ADDRESS_EXCEPTION.split(";");
        }
        send("飞鸿异常报告", to, title, ex);
    }

    private static void send(String sendername,
                             String[] to,
                             String title,
                             Throwable ex) throws Exception {
        System.setProperty("mail.mime.splitlongparameters", "false");

        Properties prop = new Properties();
        //协议
        prop.setProperty("mail.transport.protocol", "smtp");
        //服务器
        prop.setProperty("mail.smtp.host", HOST);
        //端口
        prop.setProperty("mail.smtp.port", PORT);
        //使用smtp身份验证
        prop.setProperty("mail.smtp.auth", "true");
        //使用SSL，企业邮箱必需！
        //开启安全协议
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
        } catch (GeneralSecurityException e1) {
            e1.printStackTrace();
        }
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.ssl.socketFactory", sf);

        Session session = Session.getDefaultInstance(prop, new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }

        });

//        session.setDebug(true);
        MimeMessage mimeMessage = new MimeMessage(session);
        //发件人
        mimeMessage.setFrom(new InternetAddress(USERNAME, sendername));        //可以设置发件人的别名
        //mimeMessage.setFrom(new InternetAddress(account));    //如果不需要就省略
        //收件人
        for (String emailTo : to) {
            if (StringUtils.isNotBlank(emailTo)) {
                mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo.trim()));
            }
        }
        //主题
        mimeMessage.setSubject(title);
        //时间
        mimeMessage.setSentDate(new Date());

        //容器类，可以包含多个MimeBodyPart对象
        Multipart mp = new MimeMultipart();


        //MimeBodyPart可以包装文本，图片，附件
        MimeBodyPart body = new MimeBodyPart();
        //HTML正文
        body.setContent(getExceptionTableString(ex), "text/html; charset=UTF-8");
        mp.addBodyPart(body);

        mimeMessage.setContent(mp);


        //设置邮件内容
        //仅仅发送文本
        //mimeMessage.setText(content);
        mimeMessage.saveChanges();
        Transport.send(mimeMessage);
    }

    private static String getExceptionTableString(Throwable ex) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sbHappened = new StringBuilder();
        StringBuilder sbTable = new StringBuilder();

        String happendClass = "";
        String happendNum = "";
        try {
            JSONObject exJson = JSON.parseObject(JSON.toJSONString(ex));


            String type = ex.toString();
            sb.append("<br><p>");
            sb.append("异常类：");
            sb.append(type);


            sbTable.append("<style>table{width:70%;margin:15px0;border:0;}th{background-color:#ACF3FF;color:#000000}th,td{font-size:0.95em;text-align:center;padding:4px;border-collapse:collapse;border:1pxsolid#cff8fe;border-width:1px01px0}tr{border:1pxsolid#cff8fe;}tr:nth-child(odd){background-color:#e3fbfe;}tr:nth-child(even){background-color:#fdfdfd;}</style>");
            sbTable.append("<table>");
            sbTable.append("<tr>");
            sbTable.append("<th>");
            sbTable.append("className");
            sbTable.append("</th>");

            sbTable.append("<th>");
            sbTable.append("fileName");
            sbTable.append("</th>");

            sbTable.append("<th>");
            sbTable.append("lineNumber");
            sbTable.append("</th>");

            sbTable.append("<th>");
            sbTable.append("methodName");
            sbTable.append("</th>");

            sbTable.append("<th>");
            sbTable.append("nativeMethod");
            sbTable.append("</th>");

            sbTable.append("</tr>");



            JSONArray stackTraceArr = exJson.getJSONArray("stackTrace");

            for (Object stackTrace : stackTraceArr) {
                if (stackTrace instanceof JSONObject) {
                    sbTable.append("<tr>");

                    JSONObject jsonStackTrace = (JSONObject) stackTrace;
                    String className = jsonStackTrace.getString("className");
                    String lineNumber = jsonStackTrace.getString("lineNumber");
                    sbTable.append("<td>");
                    sbTable.append(className);
                    sbTable.append("</td>");

                    sbTable.append("<td>");
                    sbTable.append(jsonStackTrace.getString("fileName"));
                    sbTable.append("</td>");

                    sbTable.append("<td>");
                    sbTable.append(lineNumber);
                    sbTable.append("</td>");

                    sbTable.append("<td>");
                    sbTable.append(jsonStackTrace.getString("methodName"));
                    sbTable.append("</td>");

                    sbTable.append("<td>");
                    sbTable.append(jsonStackTrace.getString("nativeMethod"));
                    sbTable.append("</td>");

                    sbTable.append("</tr>");

                    //检测莱米异常发生类：
                    for (String packageName : PACKAGE_LAMIC) {
                        if (className.indexOf(packageName) != -1) {
                            happendClass = className;
                            happendNum = lineNumber;
                            break;
                        }
                    }
                }
            }

            sbTable.append("</table>");

            sbHappened.append("</p><p>");
            sbHappened.append("发生类：");
            sbHappened.append(happendClass);
            sbHappened.append("</p><p>");
            sbHappened.append("发生行：");
            sbHappened.append(happendNum);
            sbHappened.append("</p>");

        } catch (Exception e) {
            sb = new StringBuilder();
            sb.append(JSON.toJSONString(ex));
        }

        sb.append(sbHappened);
        sb.append(sbTable);
        return sb.toString();
    }
}
