package com.puyixiaowo.core.thread;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.puyixiaowo.fblog.bean.NewsBean;
import com.puyixiaowo.fblog.service.fnews.NewsFilterService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * 关键词获取新闻线程
 *
 * @author Moses
 * @date 2017-12-14 10:16:39
 */
public class NewsByFilterThread extends RecursiveTask<List<NewsBean>> {


    private String page;
    private JSONObject keywords;
    private String[] includes;
    private JSONArray apiChannels;
    private boolean isMainThread;//是否主线程
    private int i;//子线程索引


    public NewsByFilterThread(String page,
                              JSONObject keywords,
                              JSONArray apiChannels,
                              int i,
                              boolean isMainThread) {
        JSONArray keywordsArr = keywords == null ? null : keywords.getJSONArray("include");

        this.page = page;
        this.keywords = keywords;
        this.apiChannels = apiChannels;
        this.isMainThread = isMainThread;
        this.i = i;

        this.includes = keywordsArr == null ? null : keywordsArr.toArray(new String[keywordsArr.size()]);
    }

    @Override
    protected List<NewsBean> compute() {

        List<NewsBean> list = new ArrayList<>();

        if (includes != null && i == includes.length) {
            //处理到结尾
            return list;
        }
        if (!isMainThread) {
            //子线程直接返回处理结果
            list = NewsFilterService.requestKeyWordsFilterNews(page, includes == null ? null : includes[i], apiChannels);

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return list;
        }

        List<NewsByFilterThread> threads = new ArrayList<>();

        for (int j = 0; j < includes.length; j++) {

            NewsByFilterThread newsByFilterThread = new NewsByFilterThread(page, keywords, apiChannels, j, false);//子线程
            threads.add(newsByFilterThread);
            newsByFilterThread.fork();
        }

        for (NewsByFilterThread newsByFilterThread :
                threads) {
            list.addAll(newsByFilterThread.join());
        }

        return list;
    }


}
