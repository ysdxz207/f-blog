package com.puyixiaowo.fblog.controller.shorlink;

import com.alibaba.fastjson.JSONObject;
import com.puyixiaowo.fblog.bean.sys.ResponseBean;
import com.puyixiaowo.fblog.enums.EnumsRedisKey;
import com.puyixiaowo.fblog.utils.IdUtils;
import com.puyixiaowo.fblog.utils.QrCodeUtils;
import com.puyixiaowo.fblog.utils.RedisUtils;
import com.puyixiaowo.fblog.utils.StringUtils;
import spark.Request;
import spark.Response;

/**
 * 
 * @author Moses
 * @date 2017-12-15 17:40
 * 
 */
public class ShortLinkController {

    private static final int TEN_DAYS = 10 * 24 * 60 * 60;


    /**
     * 短链接
     * @param request
     * @param response
     * @return
     */
    public static Object makeShortLink(Request request, Response response) {
        ResponseBean responseBean = new ResponseBean();
        String link = request.queryParams("link");

        if (StringUtils.isBlank(link)) {
            responseBean.errorMessage("没有输入链接哟");
            return responseBean;
        }

        String id = IdUtils.generateId() + "";
        String shorLink = "http://puyixiaowo.win/shortlink/" + id;
        String qrcode = QrCodeUtils.createQrcode(shorLink);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("link", link);
        jsonObject.put("shortLink", shorLink);

        RedisUtils.set(EnumsRedisKey.REDIS_KEY_SHORT_LINK.key + id,
                jsonObject.toJSONString(), TEN_DAYS);

        responseBean.setData(qrcode);

        return responseBean.serialize();
    }
}
