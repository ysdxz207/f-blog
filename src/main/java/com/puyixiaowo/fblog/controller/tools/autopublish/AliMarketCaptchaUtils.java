package com.puyixiaowo.fblog.controller.tools.autopublish;

import com.puyixiaowo.fblog.utils.FileUtils;
import com.puyixiaowo.fblog.utils.HttpUtils;
import com.puyixiaowo.fblog.utils.QrCodeUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AliMarketCaptchaUtils {

    public static String parseCaptcha(File file) {
        String url = "http://jisuyzmsb.market.alicloudapi.com/captcha/recognize";
        String appcode = "5a291bd399a340ada0898851fa7c121c";
        Map<String, String> headers = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> params = new HashMap<>();
        params.put("type", "n4");
        Map<String, String> bodys = new HashMap<>();
        bodys.put("pic", FileUtils.file2Base64(file));


        try {
            String result = HttpUtils.httpPost(url, params, headers);

            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {
//        parseCaptcha(new File("D:/captcha.png"));

        String str = FileUtils.file2Base64(new File("d:/captcha.png"));

        QrCodeUtils.base64ToImg(str, "D:/b.png");
    }
}
