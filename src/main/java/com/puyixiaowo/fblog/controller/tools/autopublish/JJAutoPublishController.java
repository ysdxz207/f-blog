package com.puyixiaowo.fblog.controller.tools.autopublish;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.puyixiaowo.fblog.bean.admin.afu.AfuBean;
import com.puyixiaowo.fblog.bean.admin.afu.AfuTypeBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.bean.sys.ResponseBean;
import com.puyixiaowo.fblog.enums.EnumsRedisKey;
import com.puyixiaowo.fblog.freemarker.FreeMarkerTemplateEngine;
import com.puyixiaowo.fblog.service.AfuTypeService;
import com.puyixiaowo.fblog.utils.DBUtils;
import com.puyixiaowo.fblog.utils.RedisUtils;
import com.puyixiaowo.fblog.utils.StringUtils;
import com.puyixiaowo.fblog.utils.sign.RSAKeyUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class JJAutoPublishController {
//    private static String URL_LOGIN_BASE = "http://puyixiaowo.win/test";
    private static String URL_LOGIN_BASE = "http://my.jjwxc.net/login.php";
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
            //获取检测时登录的cookie

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
        String pubPage = request.queryParams("jjpubpage");
        String pubTime = request.queryParams("jjpubtime");
        String captcha = request.queryParams("captcha");

        ResponseBean responseBean = new ResponseBean();
        //参数校验
        if (StringUtils.isBlank(username)) {
            return responseBean.errorMessage("请输入晋江登录用户名");
        }

        if (StringUtils.isBlank(password)) {
            return responseBean.errorMessage("请输入晋江登录密码");
        }

        if (StringUtils.isBlank(pubPage)) {
            return responseBean.errorMessage("请输入发布页面");
        }

        if (StringUtils.isBlank(pubTime)) {
            return responseBean.errorMessage("请选择发布时间");
        }

        int retryTimes = 2;


        try {
            responseBean = login(username, password, captcha, retryTimes);

            JSONObject jsonLogin = (JSONObject) responseBean.getData();

            if (jsonLogin == null) {
                return responseBean.errorMessage("服务器内部错误");
            }


            if (responseBean.getStatusCode() == 200) {

                Connection.Response res = jsonLogin.getObject("response", Connection.Response.class);
                Document document = jsonLogin.getObject("document", Document.class);
                //检测页面是否正确
                Elements elements = document.select("#publish_click");

                if (elements == null || elements.size() == 0) {
                    responseBean.setData(null);
                    return responseBean.errorMessage("未能检测通过：发布页面不正确，找不到发布按钮。");
                }

                //保存cookie到阿福
                AfuTypeBean afuTypeBean = new AfuTypeBean();
                afuTypeBean.setTag("jj_autopublish");
                PageBean pageBean = AfuTypeService.selectAfuTypePageBean(afuTypeBean, new PageBean());
                if (pageBean.getList().size() == 0) {
                    afuTypeBean.setName("晋江自动发布");

                    //自动创建一个阿福类别
                    RSAKeyUtils.RSAKey key = RSAKeyUtils.generateRSAKey();
                    afuTypeBean.setPrivateKey(key.getPrivateKey());
                    afuTypeBean.setPublicKey(key.getPublicKey());
                    DBUtils.insertOrUpdate(afuTypeBean, false);
                } else {
                    afuTypeBean = (AfuTypeBean) pageBean.getList().get(0);
                }

                AfuBean afuBean = new AfuBean();
                afuBean.setCreateTime(System.currentTimeMillis());
                afuBean.setName("晋江自动发布" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                afuBean.setType(afuTypeBean.getId());

                JSONObject json = new JSONObject();
                json.put("pubPage", pubPage);
                json.put("pubTime", pubTime);
                json.put("cookies", JSON.toJSONString(res.cookies()));

                afuBean.setContent(JSON.toJSONString(json, SerializerFeature.PrettyFormat));
                DBUtils.insertOrUpdate(afuBean, false);
            }
        } catch (Exception e) {
            responseBean.error(e);
        }
        return responseBean;
    }


    public static ResponseBean login(String username,
                                            String password,
                                            String captcha,
                                            int retryTimes) throws IOException {

        RETRY_TIMES_LAST = retryTimes > 0 ? retryTimes - 1 : RETRY_TIMES_LAST - 1;

        ResponseBean responseBean = new ResponseBean();

        JSONObject json = new JSONObject();

        Connection.Response response = login(username, password, captcha);
        response.charset(ENCODING);

        //根据cookie中是否有token判断登录是否成功
        String token = response.cookie("token");

        responseBean.setStatusCode(StringUtils.isNotBlank(token) ? 200 : 300);
        json.put("response", response);

        if (responseBean.getStatusCode() == 300) {
            //错误信息
            String message = "登录失败！";
            Document document = response.parse();

            json.put("document", document);
            Elements elements = document.select(".loginStyle");
            if (elements != null
                    && elements.size() > 0) {

                message = "登录失败，" + elements.get(0).child(0).text();
            }
            responseBean.setMessage(message);
            if (RETRY_TIMES_LAST > 0) {
                System.out.println("尝试:" + RETRY_TIMES_LAST);
                return login(username, password, captcha, RETRY_TIMES_LAST);
            }
        }

        responseBean.setData(json);
        return responseBean;
    }

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
        Connection.Response response = RedisUtils.get(EnumsRedisKey.REDIS_KEY_TOOLS_AUTOPUBLISH_CAPTCHA.key, Connection.Response.class);

        if (response == null) {
            return null;
        }
        Connection connection = Jsoup.connect(URL_LOGIN_BASE)
                .cookies(response.cookies())
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