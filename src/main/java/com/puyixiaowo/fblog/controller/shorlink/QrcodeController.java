package com.puyixiaowo.fblog.controller.shorlink;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.puyixiaowo.fblog.bean.sys.ResponseBean;
import com.puyixiaowo.fblog.enums.EnumsRedisKey;
import com.puyixiaowo.fblog.freemarker.FreeMarkerTemplateEngine;
import com.puyixiaowo.fblog.utils.IdUtils;
import com.puyixiaowo.fblog.utils.QrCodeUtils;
import com.puyixiaowo.fblog.utils.RedisUtils;
import com.puyixiaowo.fblog.utils.StringUtils;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Moses
 * @date 2017-12-15 17:40
 */
public class QrcodeController {

    private static final int TEN_DAYS = 3 * 24 * 60 * 60;

    public static Object qrcodeIndex(Request request, Response response) {
        return new FreeMarkerTemplateEngine()
                .render(new ModelAndView(null,
                        "tools/qrcode/qrcode_index.html"));
    }


    /**
     * 短链接
     *
     * @param request
     * @param response
     * @return
     */
    public static Object makeQrcode(Request request, Response response) {
        ResponseBean responseBean = new ResponseBean();
        String link = request.queryParams("link");
        Boolean isLink = Boolean.valueOf(request.queryParamOrDefault("isLink", "true"));


        if (StringUtils.isBlank(link)) {
            responseBean.errorMessage("没有输入链接哟");
            return responseBean;
        }

        HttpServletRequest req = request.raw();
        String id = IdUtils.generateId() + "";

        StringBuffer url = req.getRequestURL();
        int uriLen = req.getRequestURI().length();
        String host = url.delete(url.length() - uriLen, url.length()).append("/").toString();

        if (host.toLowerCase().indexOf("localhost") != -1) {
            InetAddress addr = null;
            int port = req.getServerPort();
            try {
                addr = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            host = "http://" + addr.getHostAddress();
            if (port != 80) {
                host += ":" + port + "/";
            }
        }
        String shorLink = host + "qrcode/" + id;
//        if (link.length() < 60) {
//            shorLink = link;
//        }
        String qrcode = QrCodeUtils.createQrcode(400, 400, shorLink);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content", link);
        jsonObject.put("isLink", isLink);

        RedisUtils.set(EnumsRedisKey.REDIS_KEY_SHORT_LINK.key + id,
                jsonObject.toJSONString(), TEN_DAYS);

        responseBean.setData("data:image/png;base64," + qrcode);

        return responseBean.serialize();
    }

    /**
     * 显示访问链接
     * @param request
     * @param response
     * @return
     */
    public static Object showQrcodeLink(Request request, Response response) {
        String id = request.params(":id");

        String str = RedisUtils.get(EnumsRedisKey.REDIS_KEY_SHORT_LINK.key + id);

        if (StringUtils.isBlank(str)) {
            Spark.internalServerError("<html><body><h1>无法访问此链接</h1></body></html>");
        }

        JSONObject jsonObject = JSON.parseObject(str);
        String content = jsonObject.getString("content");
        Boolean isLink = jsonObject.getBoolean("isLink");

        if (isLink != null && isLink) {
            if (content.toLowerCase().indexOf("http://") == -1
                    || content.toLowerCase().indexOf("https://") == -1) {
                content = "http://" + content;
            }
            response.redirect(content);
            return null;
        } else {
            return "<html><head><meta name=\"viewport\" content=\"width=device-width, height=device-height\"></head><body>" + content + "</body></html>";
        }
    }
}
