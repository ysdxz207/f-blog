package com.puyixiaowo.fblog.error;

import com.puyixiaowo.fblog.enums.EnumsRedisKey;
import com.puyixiaowo.fblog.utils.JedisUtils;
import com.puyixiaowo.fblog.utils.ResourceUtils;
import spark.utils.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class ErrorHandler {

    public static String handle404(){

        String html = JedisUtils.get(EnumsRedisKey.REDIS_KEY_404_PAGE_KEY.key);
        if (StringUtils.isNotBlank(html)) {
            return html;
        }
        try {
            InputStream inputStream = ResourceUtils.readFile("error/404.html");

            Scanner sc = new Scanner(inputStream, "UTF-8");
            StringBuilder sb = new StringBuilder();
            while (sc.hasNextLine()) {
                sb.append(sc.nextLine());
            }
            // note that Scanner suppresses exceptions
            if (sc.ioException() != null) {
                throw sc.ioException();
            }

            JedisUtils.set(EnumsRedisKey.REDIS_KEY_404_PAGE_KEY.key, sb.toString());
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            JedisUtils.set(EnumsRedisKey.REDIS_KEY_404_PAGE_KEY.key, "404");
            return "404";
        }
    }
}
