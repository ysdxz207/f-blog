package com.puyixiaowo.fblog.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.puyixiaowo.fblog.exception.TimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpUtils {

    private static final String CHARSET = "UTF-8";

    /**
     * httpPost
     *
     * @param url    路径
     * @param params 参数
     * @return
     */
    public static JSONObject httpPost(String url, Map<String, String> params) throws TimeoutException {
        PostMethod post = new PostMethod(url);
        return request(post);
    }

    /**
     * httpGet
     *
     * @param url    路径
     * @param params 参数
     * @return
     */
    public static JSONObject httpGet(String url) throws TimeoutException {
        GetMethod get = new GetMethod(url);
        return request(get);
    }

    private static JSONObject request(HttpMethod method) {
        method.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        JSONObject json = new JSONObject();
        HttpClient httpClient = new HttpClient();

        try {
            InputStream in = null;
            int statusCode = httpClient.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                return json;
            }
            in = method.getResponseBodyAsStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, CHARSET));
            String str = reader.lines().collect(Collectors.joining("\n"));
            if (StringUtils.isBlank(str)) {
                return json;
            }
            return JSON.parseObject(str);
        } catch (IOException e) {
            throw new TimeoutException("请求失败");
        }
    }


}
