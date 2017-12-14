package com.puyixiaowo.fblog.controller.fnews;

import com.alibaba.fastjson.JSONObject;
import com.puyixiaowo.core.timer.TimerFetchNews;
import com.puyixiaowo.fblog.annotation.admin.RequiresPermissions;
import com.puyixiaowo.fblog.bean.NewsBean;
import com.puyixiaowo.fblog.bean.admin.UserBean;
import com.puyixiaowo.fblog.bean.admin.UserRoleBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.bean.sys.ResponseBean;
import com.puyixiaowo.fblog.constants.Constants;
import com.puyixiaowo.fblog.controller.BaseController;
import com.puyixiaowo.fblog.enums.EnumsRedisKey;
import com.puyixiaowo.fblog.freemarker.FreeMarkerTemplateEngine;
import com.puyixiaowo.fblog.service.RolePermissionService;
import com.puyixiaowo.fblog.service.UserRoleService;
import com.puyixiaowo.fblog.service.UserService;
import com.puyixiaowo.fblog.service.fnews.NewsFilterService;
import com.puyixiaowo.fblog.utils.DBUtils;
import com.puyixiaowo.fblog.utils.DesUtils;
import com.puyixiaowo.fblog.utils.RedisUtils;
import com.puyixiaowo.fblog.utils.StringUtils;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;

/**
 *
 */
public class FNewsController extends BaseController{


    public static String news(Request request,
                                       Response response){

        Boolean data = Boolean.valueOf(request.params(":data"));

        if (!data) {
            return new FreeMarkerTemplateEngine()
                    .render(new ModelAndView(null,
                            "fnews/news_index.html"));
        }

        PageBean<NewsBean> pageBean = getPageBean(request);
        try {
            NewsBean newsBean = getParamsEntity(request, NewsBean.class, true);
            int page = pageBean.getPageCurrent();
            String channelId = newsBean.getChannelId();
            List<NewsBean> list;
            if (page > TimerFetchNews.CACHE_PAGE_SIZE) {
                JSONObject channel = NewsFilterService.getChannelByChannelId(channelId);
                list = NewsFilterService.fetchChannelNews(page + "", channel);
            } else {

                list = RedisUtils.getList(EnumsRedisKey.REDIS_KEY_FNEWS_LIST.key + newsBean.getChannelId() + "_" + page, NewsBean.class);
            }
            pageBean.setList(list);
        } catch (Exception e) {
            pageBean.error(e);
        }
        return pageBean.serialize();
    }

}
