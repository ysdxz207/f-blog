package com.puyixiaowo.fblog.controller.tools.autopublish;

import com.puyixiaowo.fblog.bean.sys.ResponseBean;
import com.puyixiaowo.fblog.freemarker.FreeMarkerTemplateEngine;
import com.puyixiaowo.fblog.utils.StringUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class JJAutoPublishController {
    private static String URL_LOGIN_BASE = "http://puyixiaowo.win/test";
//    private static String URL_LOGIN_BASE = "http://my.jjwxc.net/login.php";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36";
    private static final String URL_CAPTCHA = "http://my.jjwxc.net/include/checkImage.php?random=" + Math.random();

    private static final String ENCODING = "GB2312";

    private static int RETRY_TIMES_LAST = 0;

    /**
     * actions
     */
    private static final String ACTION_LOGIN = "login";


    public static Object autoPublish(Request request, Response response) {
        String username = "dx87fi@163.com";
        String password = "meixianghao";

        ResponseBean responseBean = new ResponseBean();

        Boolean data = Boolean.valueOf(request.queryParamOrDefault("data", "false"));

        if (!data) {
            return new FreeMarkerTemplateEngine()
                    .render(new ModelAndView(null,
                            "tools/autopublish/index_autopublish.html"));
        }

        try {
            responseBean = login(username, password, RETRY_TIMES_LAST);

            if (responseBean.getStatusCode() == 200) {
                System.out.println("[信息]请粘贴编辑(发布)文章页面：");
                String editArticlePage = new Scanner(System.in).next();
                Connection.Response loginResponse = (Connection.Response) responseBean.getData();
                Connection.Response res = accessPage(loginResponse, editArticlePage);

                //检测页面是否正确
                Elements elements = res.parse().select("#publish_click");

                if (elements.size() == 0) {
                    responseBean.errorMessage("[错误]发布页面不正确");
//                return responseBean;
                }

                //进入定时发布


            } else {
                System.out.println("[信息]登录失败");
            }
        } catch (Exception e) {
            responseBean.error(e);
        }
        return responseBean;
    }

    public static Object checkConfig(Request request, Response response) {
        String username = request.queryParams("jjusername");
        String password = request.queryParams("jjpassword");
        int retryTimes = 2;

        ResponseBean responseBean = new ResponseBean();

        try {
            responseBean = login(username, password, retryTimes);
        } catch (Exception e) {
            responseBean.error(e);
        }
        return responseBean;
    }


    public static ResponseBean login(String username,
                                            String password,
                                            int retryTimes) throws IOException {

        RETRY_TIMES_LAST = retryTimes > 0 ? retryTimes - 1 : RETRY_TIMES_LAST - 1;

        ResponseBean responseBean = new ResponseBean();


        Connection.Response response = login(username, password);
        response.charset(ENCODING);

        //根据cookie中是否有token判断登录是否成功
        String token = response.cookie("token");

        responseBean.setStatusCode(StringUtils.isNotBlank(token) ? 200 : 300);
        responseBean.setData(response);

        if (responseBean.getStatusCode() == 300) {
            //错误信息
            String message = "登录失败！";
            Elements elements = response.parse().select(".loginStyle");
            if (elements != null
                    && elements.size() > 0) {

                message = elements.get(0).child(0).text();
            }
            responseBean.setMessage(message);
            if (RETRY_TIMES_LAST > 0) {
                System.out.println("尝试:" + RETRY_TIMES_LAST);
                return login(username, password, RETRY_TIMES_LAST);
            }
        }
        return responseBean;
    }

    public static Connection.Response login(String username,
                                            String password) throws IOException {

        String captchaNum = "";
        ResponseBean captchaPicBean = getCaptchaPic();

        Scanner sc = new Scanner(System.in);
        System.out.print("[信息]请输入验证码：");
        captchaNum = sc.next();


        Map<String, String> params = new HashMap<>();
        params.put("action", ACTION_LOGIN);
        params.put("loginname", username);
        params.put("loginpassword", password);
        params.put("cookietime", "0");
        params.put("client_time", System.currentTimeMillis() / 1000 + "");
        params.put("auth_num", captchaNum);


        Connection connection = Jsoup.connect(URL_LOGIN_BASE)
                .cookies(((Connection.Response)captchaPicBean.getData()).cookies())
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

        //以下测试

        File file = new File("D:/captcha.jpg");

        OutputStream outputStream = new FileOutputStream(file);
        IOUtils.write(response.bodyAsBytes(), outputStream);
        //判断是否支持Desktop扩展,如果支持则进行下一步
        if (Desktop.isDesktopSupported()){
            Desktop desktop = Desktop.getDesktop(); //创建desktop对象
            desktop.open(file);
        }

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