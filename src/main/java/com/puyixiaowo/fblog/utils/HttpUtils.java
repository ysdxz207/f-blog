package com.puyixiaowo.fblog.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.puyixiaowo.fblog.exception.TimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
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
     * @param url       路径
     * @param params 参数
     * @return
     */
    public static JSONObject httpPost(String url, Map<String, String> params) throws TimeoutException{
        JSONObject json = new JSONObject();
        PostMethod post = new PostMethod(url);
        post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        HttpClient httpClient = new HttpClient();

        List<NameValuePair> nvpList = new ArrayList<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            nvpList.add(new NameValuePair(entry.getKey(), entry.getValue()));
        }
        NameValuePair [] arr = new NameValuePair[nvpList.size()];
        post.setRequestBody(nvpList.toArray(arr));
        InputStream in = null;
        try {
            int statusCode = httpClient.executeMethod(post);
            if (statusCode != HttpStatus.SC_OK) {
                return json;
            }
            in = post.getResponseBodyAsStream();
            BufferedReader reader =  new BufferedReader(new InputStreamReader(in, CHARSET));
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
