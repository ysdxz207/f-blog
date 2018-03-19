package com.puyixiaowo.fblog.controller.tools.autopublish;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.puyixiaowo.fblog.bean.admin.afu.AfuBean;
import com.puyixiaowo.fblog.bean.admin.afu.AfuTypeBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.bean.sys.ResponseBean;
import com.puyixiaowo.fblog.controller.tools.autopublish.timer.TimerKeepJJSession;
import com.puyixiaowo.fblog.controller.tools.autopublish.utils.JJAutoPushUtils;
import com.puyixiaowo.fblog.enums.EnumsRedisKey;
import com.puyixiaowo.fblog.freemarker.FreeMarkerTemplateEngine;
import com.puyixiaowo.fblog.service.AfuTypeService;
import com.puyixiaowo.fblog.utils.DBUtils;
import com.puyixiaowo.fblog.utils.RedisUtils;
import com.puyixiaowo.fblog.utils.StringUtils;
import com.puyixiaowo.fblog.utils.sign.RSAKeyUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class JJAutoPublishController {


    public static Object autoPublish(Request request, Response response) {

        ResponseBean responseBean = new ResponseBean();

        Boolean data = Boolean.valueOf(request.queryParamOrDefault("data", "false"));

        if (!data) {

            Map<String, Object> model = new HashMap<>();

            model.put("jjusername", RedisUtils.get(EnumsRedisKey.REDIS_KEY_TOOLS_AUTOPUBLISH_LOGIN_USERNAME.key));
            model.put("jjpubpage", RedisUtils.get(EnumsRedisKey.REDIS_KEY_TOOLS_AUTOPUBLISH_LOGIN_PUBPAGE.key));
            return new FreeMarkerTemplateEngine()
                    .render(new ModelAndView(model,
                            "tools/autopublish/index_autopublish.html"));
        }

        try {
            if (responseBean.getStatusCode() == 200) {
                //进入定时发布
                new TimerKeepJJSession().start();

            } else {
                System.out.println("[信息]登录失败");
            }
        } catch (Exception e) {
            responseBean.error(e);
        }
        return responseBean;
    }


    public static Object getCaptcha(Request request, Response response) {

        ResponseBean responseBean = new ResponseBean();

        try {
            responseBean = JJAutoPushUtils.getCaptchaPic();
            if (responseBean.getStatusCode() == 200) {
                //存入redis
                RedisUtils.set(EnumsRedisKey.REDIS_KEY_TOOLS_AUTOPUBLISH_CAPTCHA_COOKIES.key,
                        JSON.toJSONString(((Connection.Response)responseBean.getData()).cookies()),
                        60 * 60);
            }
        } catch (Exception e) {
            responseBean.error(e);
        }
        return responseBean;
    }


    public static Object checkConfig(Request request, Response response) {
        String username = request.queryParams("jjusername");
        String password = request.queryParams("jjpassword");
        String pubPage = request.queryParams("jjpubpage");
        String pubTime = request.queryParams("jjpubtime");
        String captcha = request.queryParams("captcha");

        ResponseBean responseBean = new ResponseBean();
        //参数校验
        if (StringUtils.isBlank(username)) {
            return responseBean.errorMessage("请输入晋江登录用户名");
        }

        if (StringUtils.isBlank(password)) {
            return responseBean.errorMessage("请输入晋江登录密码");
        }

        if (StringUtils.isBlank(pubPage)) {
            return responseBean.errorMessage("请输入发布页面");
        }

        if (StringUtils.isBlank(pubTime)) {
            return responseBean.errorMessage("请选择发布时间");
        }

        //处理protocal
        if (pubPage.indexOf("http://") == -1
                && pubPage.indexOf("https://") == -1) {
            return responseBean.errorMessage("发布页面格式不正确");
        }

        //记住帐号
        RedisUtils.set(EnumsRedisKey.REDIS_KEY_TOOLS_AUTOPUBLISH_LOGIN_USERNAME.key,
                username);
        RedisUtils.set(EnumsRedisKey.REDIS_KEY_TOOLS_AUTOPUBLISH_LOGIN_PUBPAGE.key,
                pubPage);

        try {
            responseBean = JJAutoPushUtils.login(username, password, captcha);

            if (responseBean.getStatusCode() == 200) {


                //检测发布页面
                Connection.Response res = JJAutoPushUtils.accessPage((Map<String, String>)responseBean.getData(),
                        pubPage);

                //检测页面是否正确
                Elements elements = res.parse().select("#publish_click");

                if (elements == null || elements.size() == 0) {
                    responseBean.setData(null);
                    return responseBean.errorMessage("未能检测通过：发布页面不正确，找不到发布按钮。");
                }

                //保存cookie到阿福
                AfuTypeBean afuTypeBean = new AfuTypeBean();
                afuTypeBean.setTag("jj_autopublish");
                PageBean pageBean = AfuTypeService.selectAfuTypePageBean(afuTypeBean, new PageBean());
                if (pageBean.getList().size() == 0) {
                    afuTypeBean.setName("晋江自动发布");

                    //自动创建一个阿福类别
                    RSAKeyUtils.RSAKey key = RSAKeyUtils.generateRSAKey();
                    afuTypeBean.setPrivateKey(key.getPrivateKey());
                    afuTypeBean.setPublicKey(key.getPublicKey());
                    DBUtils.insertOrUpdate(afuTypeBean, false);
                } else {
                    afuTypeBean = (AfuTypeBean) pageBean.getList().get(0);
                }

                AfuBean afuBean = new AfuBean();
                afuBean.setCreateTime(System.currentTimeMillis());
                afuBean.setName("晋江自动发布" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                afuBean.setType(afuTypeBean.getId());

                JSONObject json = new JSONObject();
                json.put("pubPage", pubPage);
                json.put("pubTime", pubTime);
                json.put("cookies", JSON.toJSONString(res.cookies()));

                afuBean.setContent(JSON.toJSONString(json, SerializerFeature.PrettyFormat));
                DBUtils.insertOrUpdate(afuBean, false);
            }
        } catch (Exception e) {
            responseBean.error(e);
        }
        return responseBean;
    }

}