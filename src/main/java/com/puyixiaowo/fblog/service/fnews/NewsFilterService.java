package com.puyixiaowo.fblog.service.fnews;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.puyixiaowo.core.thread.NewsByFilterThread;
import com.puyixiaowo.core.timer.TimerFetchNews;
import com.puyixiaowo.fblog.bean.NewsBean;
import com.puyixiaowo.fblog.enums.EnumsRedisKey;
import com.puyixiaowo.fblog.utils.HttpUtils;
import com.puyixiaowo.fblog.utils.RedisUtils;
import com.puyixiaowo.fblog.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

/**
 * @author Moses
 * @date 2017-12-14
 */
public class NewsFilterService {
    private static final Logger logger = LoggerFactory.getLogger(NewsFilterService.class);


    private static final String NEWS_API_URL = "http://route.showapi.com/109-35";
    private static final String APIKEY = "955daa5dd0644ca796aaa978c4c951aa";
    private static final String APPID = "24272";


    public static void fetchNews(int pageSize) {
        for (int i = 0; i < pageSize; i++) {

            String page = i +1 + "";

            for (Object channelObj :
                    TimerFetchNews.CHANNELS) {
                JSONObject channel = (JSONObject) channelObj;
                String channelId = channel.getString("id");
                List<NewsBean> list = fetchChannelNews(page, channel);
                RedisUtils.set(EnumsRedisKey.REDIS_KEY_FNEWS_LIST.key + channelId + "_" + page,
                        JSON.toJSONString(list));
            }
        }

    }

    public static List<NewsBean> fetchChannelNews(String page,
                                         JSONObject channel) {

            String channelId = channel.getString("id");
            JSONArray apiChannels = channel.getJSONArray("apiChannels");
            JSONObject channelKeywords = getChannelKeywordsByChannelId(channelId);

            boolean isMainThread = channelKeywords != null;

            List<NewsBean> result = new ArrayList<NewsBean>();
            // 创建一个通用池
            ForkJoinPool pool = ForkJoinPool.commonPool();

            // 提交可分解的CalTask任务
            try {
                Future<List<NewsBean>> future = pool.submit(new NewsByFilterThread(page, channelKeywords, apiChannels, 0, isMainThread));
                result = future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            return result;
    }


    private static JSONObject getChannelKeywordsByChannelId(String channelId) {
        for (Object keywordsObj : TimerFetchNews.KEYWORDS) {
            JSONObject keywords = (JSONObject) keywordsObj;
            if (channelId.equalsIgnoreCase(keywords.getString("id"))
                    && keywords.getJSONArray("include") != null) {
                return keywords;
            }
        }
        return null;
    }

    public static List<NewsBean> requestKeyWordsFilterNews(String page,
                                                           String keyword,
                                                           JSONArray channelIds) {


        List<NewsBean> list = new ArrayList<>();


        for (Object channelObj : channelIds) {
            JSONObject channel = (JSONObject) channelObj;
            String apiChannelId = channel.getString("id");
            if (StringUtils.isBlank(apiChannelId)) {
                continue;
            }
            list.addAll(requestNewsByKeywordAndChannels(page, keyword, apiChannelId));
        }

        return list;
    }

    private static List<NewsBean> requestNewsByKeywordAndChannels(String page,
                                                                  String keyword,
                                                                  String apiChannelId) {

        List<NewsBean> list = new ArrayList<>();

        Map<String, String> params = new HashMap<>();
        params.put("needHtml", "1");
        params.put("showapi_res_gzip", "0");
        params.put("page", page);
        params.put("maxResult", "20");
        params.put("channelId", apiChannelId);
        if (StringUtils.isNotBlank(keyword)) {
            params.put("title", keyword);
        }
        params.put("showapi_appid", APPID);
        params.put("showapi_sign", APIKEY);

        JSONObject jsonResult = HttpUtils.httpPost(NEWS_API_URL, params);

        if (StringUtils.isBlank(jsonResult)) {
            logger.error("新闻接口返回结果为空");
            return list;
        }

        JSONObject showApiResBody = jsonResult.getJSONObject("showapi_res_body");

        if (StringUtils.isBlank(showApiResBody)) {
            logger.error("新闻接口返回错误：" + jsonResult);
            return list;
        }

        Integer retCode = showApiResBody.getInteger("ret_code");


        if (retCode == null || retCode != 0) {
            logger.error("新闻接口返回错误：" + jsonResult.getString("showapi_res_error"));
            return list;
        }

        JSONObject pageBean = showApiResBody.getJSONObject("pagebean");

        JSONArray contentList = pageBean.getJSONArray("contentlist");

        if (contentList == null || contentList.size() == 0) {
            return list;
        }

        list.addAll(contentList.toJavaList(NewsBean.class));

        return list;
    }

    public static JSONObject getChannelByChannelId(String channelId) {
        for (Object obj : TimerFetchNews.CHANNELS) {
            JSONObject channel = (JSONObject) obj;
            if (channelId.equalsIgnoreCase(channel.getString("id"))) {
                return channel;
            }
        }
        return null;
    }

}
