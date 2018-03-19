package com.puyixiaowo.fblog.controller.tools.autopublish.utils;

import com.puyixiaowo.fblog.bean.sys.ResponseBean;
import com.puyixiaowo.fblog.enums.EnumsRedisKey;
import com.puyixiaowo.fblog.utils.RedisUtils;
import org.apache.commons.codec.binary.Base64;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JJAutoPushUtils {

    private static String URL_LOGIN_BASE = "http://my.jjwxc.net/login.php";
    private static final String URL_CAPTCHA = "http://my.jjwxc.net/include/checkImage.php?random=" + Math.random();
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36";
    public static final String ENCODING = "GB2312";

    /**
     * actions
     */
    private static final String ACTION_LOGIN = "login";


    public static Connection.Response login(String username,
                                            String password,
                                            String captcha) throws IOException {

        Map<String, String> params = new HashMap<>();
        params.put("action", ACTION_LOGIN);
        params.put("loginname", username);
        params.put("loginpassword", password);
        params.put("cookietime", "0");
        params.put("client_time", System.currentTimeMillis() / 1000 + "");
        params.put("auth_num", captcha);

        //获取验证码cookie
        Map<String, String> cookies = RedisUtils.get(EnumsRedisKey.REDIS_KEY_TOOLS_AUTOPUBLISH_CAPTCHA.key, Map.class);

        if (cookies == null) {
            return null;
        }
        Connection connection = Jsoup.connect(URL_LOGIN_BASE)
                .cookies(cookies)
                .userAgent(USER_AGENT)
                .data(params)
                .method(Connection.Method.GET);


        return connection.execute();
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


    public static Connection.Response accessPage(Connection.Response loginResponse,
                                                 String url) throws IOException {
        Connection connection = Jsoup.connect(url)
                .cookies(loginResponse.cookies())
                .userAgent(USER_AGENT)
                .ignoreContentType(true)
                .method(Connection.Method.GET);

        Connection.Response response = connection.execute();
        response.charset(ENCODING);
        System.out.println(response.body());
        return response;
    }
}
