package com.puyixiaowo.fblog.controller.tools.autopublish;

import com.puyixiaowo.fblog.utils.FileUtils;
import com.puyixiaowo.fblog.utils.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class LoginTest {
    private static final String URL_PAGE_LOGIN = "http://my.jjwxc.net/login.php";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0";
    private static final String URL_CAPTCHA = "http://my.jjwxc.net/include/checkImage.php?random=" + Math.random();
    private static final String FILE_NAME_TEMP_CAPTCHA_IMG = "D:/captcha.jpg";
//    private static final String FILE_NAME_TEMP_CAPTCHA_IMG = System.getProperty("java.io.tmpdir") + "fblog/tools/autopublish/captcha.jpg";

    public static void main(String[] args) throws Exception {
        LoginTest loginDemo = new LoginTest();
        Connection.Response result = loginDemo.login("dx87fi@163.com", "meixianghao", true);

        System.out.println(result.body());
    }


    public static Connection.Response login(String username,
                                            String password,
                                            boolean reTry) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("loginname", username);
        params.put("loginpassword", password);


        Connection connection = Jsoup.connect(URL_PAGE_LOGIN + "?action=login&referer=http://my.jjwxc.net/backend/logininfo.php")
                .header("User-Agent",
                        USER_AGENT)
                .data(params)
                .cookie("token", "")
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
        if (needCaptcha) {
            return loginWithCaptcha(username, password, reTry);
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

            File captchaFile = new File(FILE_NAME_TEMP_CAPTCHA_IMG);
            if (!captchaFile.getParentFile().exists()) {
                captchaFile.getParentFile().mkdirs();
            }
            FileUtils.copyFile(file, captchaFile);


        } catch (Exception e) {

        }
    }
}