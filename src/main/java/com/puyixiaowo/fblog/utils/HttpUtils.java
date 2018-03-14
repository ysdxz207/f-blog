package com.puyixiaowo.fblog.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.puyixiaowo.fblog.exception.TimeoutException;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

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
    private static final int TIMEOUT_REQUEST = 5000;

    /**
     * httpPost
     *
     * @param url    路径
     * @param params 参数
     * @return
     */
    public static String httpPost(String url, Map<String, String> params) throws TimeoutException {
        PostMethod post = new PostMethod(url);
        post.setRequestHeader("Content-Type",
                "application/x-www-form-urlencoded;charset=UTF-8");

        if (params != null && params.size() > 0) {
            post.setRequestBody(getParams(params));
        }

        return request(post);
    }

    /**
     * httpGet
     *
     * @param url    路径
     * @return
     */
    public static String httpGet(String url, Map<String, String> params) throws TimeoutException {
        GetMethod get = new GetMethod(url);

        if (params != null && params.size() > 0) {

            get.setQueryString(getParams(params));
        }
        return request(get);
    }


    private static NameValuePair [] getParams(Map<String, String> params) {
        List<NameValuePair> nvpList = new ArrayList<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            nvpList.add(new NameValuePair(entry.getKey(), entry.getValue()));
        }
        NameValuePair [] arr = new NameValuePair[nvpList.size()];
        return nvpList.toArray(arr);
    }

    private static String request(HttpMethod method) {
        method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT,TIMEOUT_REQUEST);
        method.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        String str = null;
        HttpClient httpClient = new HttpClient();

        try {
            InputStream in = null;
            int statusCode = httpClient.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                return str;
            }
            in = method.getResponseBodyAsStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, CHARSET));
            str = reader.lines().collect(Collectors.joining("\n"));
            if (StringUtils.isBlank(str)) {
                return str;
            }
            return str;
        } catch (IOException e) {
            throw new TimeoutException("请求接口失败:" + e.getMessage());
        }
    }


}
