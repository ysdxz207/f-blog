package com.puyixiaowo.fblog.controller.tools.autopublish;

import com.puyixiaowo.fblog.utils.FileUtils;
import com.puyixiaowo.fblog.utils.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class LoginTest {
    private static final String URL_PAGE_LOGIN = "http://my.jjwxc.net/login.php";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36";
    private static final String URL_CAPTCHA = "http://my.jjwxc.net/include/checkImage.php?random=" + Math.random();

    public static void main(String[] args) throws Exception {
        LoginTest loginDemo = new LoginTest();
        Connection.Response result = loginDemo.login("dx87fi@163.com", "meixianghao", true);

        System.out.println(result.body());
    }


    public static Connection.Response login(String username,
                                            String password,
                                            boolean reTry) throws IOException {
        //http://static.jjwxc.net/scripts/jjlogin.js?ver=20180211
        Map<String, String> params = new HashMap<>();
        params.put("loginname", username);
        params.put("loginpassword", URLEncoder.encode("logindev" + password, "UTF-8"));


        Connection connection = Jsoup.connect(URL_PAGE_LOGIN + "?action=login&referer=http://my.jjwxc.net/backend/logininfo.php")
                .data(params)
                .userAgent(USER_AGENT)
                .cookie("token", "")
                .cookie("login_need_authnum", "")
                .method(Connection.Method.POST);

        Connection.Response res = connection.execute();

        boolean loginSuccess = StringUtils.isNotBlank(res.cookie("token"));
        boolean needCaptcha = "1".equals(res.cookie("login_need_authnum"));

        if (loginSuccess){
            return res;
        }

        if (!reTry) {
            return res;
        }

        if (needCaptcha) {

            res = loginWithCaptcha(username, password, reTry);
        } else {
            login(username, password, reTry);
        }
        return res;
    }

    public static Connection.Response loginWithCaptcha(String username,
                                                       String password,
                                                       boolean reTry) throws IOException {
        getCaptchaPic();
        Map<String, String> params = new HashMap<>();
        params.put("loginname", username);
        params.put("loginpassword", password);

        Scanner sc = new Scanner(System.in);
        System.out.print("请输入验证码：");
        String captchaNum = "";
        captchaNum = sc.next();

        params.put("auth_num", captchaNum);

        Connection connection = Jsoup.connect(URL_PAGE_LOGIN + "?action=login&referer=http://my.jjwxc.net/backend/logininfo.php")
                .header("User-Agent",
                        USER_AGENT)
                .data(params)
                .method(Connection.Method.POST);
        connection.data(params);


        Connection.Response res = connection.execute();

        boolean loginSuccess = StringUtils.isNotBlank(res.cookie("token"));
        boolean needCaptcha = "1".equals(res.cookie("login_need_authnum"));

        if (loginSuccess) {
            return res;
        }
        if (needCaptcha && reTry) {
            return loginWithCaptcha(username, password, reTry);
        }

        if (!needCaptcha && reTry) {
            return login(username, password, reTry);
        }
        return res;
    }

    public static void getCaptchaPic() {
        try {
            // 构造URL
            URL url = new URL(URL_CAPTCHA);
            // 打开连接
            URLConnection con = url.openConnection();
            // 输入流
            InputStream is = con.getInputStream();
            File file = FileUtils.stream2file(is);

            String captchaBase64 = "data:image/png;base64," + FileUtils.file2Base64(file);

            //以下测试

            File fileCaptcha = new File("D:/captcha.jpg");
            FileUtils.copyFile(file, fileCaptcha);
            //判断是否支持Desktop扩展,如果支持则进行下一步
            if (Desktop.isDesktopSupported()){
                Desktop desktop = Desktop.getDesktop(); //创建desktop对象
                desktop.open(fileCaptcha);
            }

        } catch (Exception e) {

        }
    }
}