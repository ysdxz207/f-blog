package com.puyixiaowo.fblog.controller.tools.autopublish.utils;

import com.alibaba.fastjson.JSONObject;
import com.puyixiaowo.fblog.bean.sys.ResponseBean;
import com.puyixiaowo.fblog.enums.EnumsRedisKey;
import com.puyixiaowo.fblog.utils.RedisUtils;
import com.puyixiaowo.fblog.utils.StringUtils;
import org.apache.commons.codec.binary.Base64;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JJAutoPushUtils {

    public static String URL_LOGIN_BASE = "http://my.jjwxc.net/login.php";
    private static final String URL_CAPTCHA = "http://my.jjwxc.net/include/checkImage.php?random=" + Math.random();
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36";
    public static final String ENCODING = "GB2312";


    /**
     * actions
     */
    public static final String ACTION_LOGIN = "login";


    public static ResponseBean login(String username,
                                     String password,
                                     String captcha) throws IOException {
        ResponseBean responseBean = new ResponseBean();

        //如果已经登录成功过则直接取cookies
//        Map<String, String> cookiesLogin = RedisUtils.get(EnumsRedisKey.REDIS_KEY_TOOLS_AUTOPUBLISH_LOGIN_COOKIES.key,
//                Map.class);
//
//        if (cookiesLogin != null && cookiesLogin.size() > 0) {
//
//            responseBean.setData(cookiesLogin);
//            return responseBean;
//        }


        //登录

        Map<String, String> params = new HashMap<>();
        params.put("action", JJAutoPushUtils.ACTION_LOGIN);
        params.put("loginname", username);
        params.put("loginpassword", password);
        params.put("cookietime", "0");
        params.put("client_time", System.currentTimeMillis() / 1000 + "");
        params.put("auth_num", captcha);

        //获取验证码cookie
        Map<String, String> cookiesCaptcha = RedisUtils.get(EnumsRedisKey.REDIS_KEY_TOOLS_AUTOPUBLISH_CAPTCHA_COOKIES.key, Map.class);

        if (cookiesCaptcha == null) {
            return responseBean;
        }

        Connection connection = Jsoup.connect(JJAutoPushUtils.URL_LOGIN_BASE)
                .cookies(cookiesCaptcha)
                .userAgent(JJAutoPushUtils.USER_AGENT)
                .data(params)
                .method(Connection.Method.GET);



        Connection.Response response = connection.execute();
        response.charset(JJAutoPushUtils.ENCODING);

        //根据cookie中是否有token判断登录是否成功
        String token = response.cookie("token");

        responseBean.setStatusCode(StringUtils.isNotBlank(token) ? 200 : 300);
        Document document = response.parse();

        if (responseBean.getStatusCode() == 200) {
            responseBean.setData(response.cookies());
//            RedisUtils.set(EnumsRedisKey.REDIS_KEY_TOOLS_AUTOPUBLISH_LOGIN_COOKIES.key,
//                    JSON.toJSONString(response.cookies()), 60 * 60 * 24);
        } else {
            //错误信息
            String message = "登录失败！";

            Elements elements = document.select(".loginStyle");
            if (elements != null
                    && elements.size() > 0) {

                message = "登录失败，" + elements.get(0).child(0).text();
            }
            responseBean.setMessage(message);
        }

        return responseBean;
    }

    public static ResponseBean getCaptchaPic() throws IOException {

        ResponseBean responseBean = new ResponseBean();
        Connection connection = Jsoup.connect(URL_CAPTCHA)
                .userAgent(USER_AGENT)
                .ignoreContentType(true)
                .method(Connection.Method.GET);

        Connection.Response response = connection.execute();

        responseBean.setData(response);

        //获取图片
        responseBean.setMessage(Base64.encodeBase64String(response.bodyAsBytes()));
        return responseBean;
    }


    public static Connection.Response accessPage(Map<String, String> loginCookies,
                                                 String url) throws IOException {
        Connection connection = Jsoup.connect(url)
                .cookies(loginCookies)
                .userAgent(USER_AGENT)
                .ignoreContentType(true)
                .method(Connection.Method.GET);

        Connection.Response response = connection.execute();
        response.charset(ENCODING);
        return response;
    }
}
