package com.puyixiaowo.fblog.controller.tools.autopublish;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws IOException {

        String jsonstr = "{\"nicknameAndsign\":\"2%257E%2529%2524%25E5%2593%2594%25E5%2590%25A7\",\"token\":\"MjU3OTY0NDF8MjVjMjY3N2Q0YWM5NGJlY2E4NzE3MDAzNDI5MzQ5Yzd8fGR4OCoqKkAxNjMuY29tfDIyMDkwNzV8MTA4MDB8MXzlk5TlkKd8fOasoui%2FjuaCqO%2B8jOaZi%2Baxn%2BeUqOaIt3wwfGVtYWls\",\"JJEVER\":\"%7B%22ispayuser%22%3A%2225796441-0%22%2C%22foreverreader%22%3A%2225796441%22%7D\"}\n";

        JSONObject json = JSON.parseObject(jsonstr);
        Map<String, String> cookies = json.toJavaObject(Map.class);

        Connection connection = Jsoup.connect("http://my.jjwxc.net/backend/logininfo.php")
                .cookies(cookies)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36")
                .ignoreContentType(true)
                .method(Connection.Method.GET);

        Connection.Response response = connection.execute();
        response.charset("GB2312");
        System.out.println(response.body());
    }
}
