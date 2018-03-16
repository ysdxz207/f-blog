package com.puyixiaowo.fblog.controller.tools.autopublish;

import com.alibaba.fastjson.JSON;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginDemo {
    private static final String LOGIN_PAGE = "http://my.jjwxc.net/login.php";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0";


    public static void main(String[] args) throws Exception {
        LoginDemo loginDemo = new LoginDemo();
        loginDemo.login("dx87fi@163.com", "meixianghao");
    }

    public void login(String username, String password) throws Exception {

        Map<String, String> params = new HashMap<>();
        params.put("loginname", username);
        params.put("loginpassword", password);

        Connection.Response res = Jsoup.connect(LOGIN_PAGE + "?action=login&referer=")
                .header("User-Agent",
                USER_AGENT)
                .data(params)
                .cookie("login_need_authnum", "0")
                .method(Connection.Method.POST)
                .execute();

        //这儿的SESSIONID需要根据要登录的目标网站设置的session Cookie名字而定
        String sessionId = res.cookie("SESSIONID");

        Document document = Jsoup.connect("http://my.jjwxc.net/backend/logininfo.php")
                .header("User-Agent",
                USER_AGENT)
                .cookies(res.cookies())
                .get();
        System.out.println(document);
    }
}