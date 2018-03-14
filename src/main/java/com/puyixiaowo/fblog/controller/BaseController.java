package com.puyixiaowo.fblog.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.puyixiaowo.core.entity.Validatable;
import com.puyixiaowo.core.exceptions.ValidationException;
import com.puyixiaowo.fblog.bean.AccessRecordBean;
import com.puyixiaowo.fblog.bean.admin.afu.AfuTypeBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.constants.Constants;
import com.puyixiaowo.fblog.controller.fblog.FblogController;
import com.puyixiaowo.fblog.exception.BaseControllerException;
import com.puyixiaowo.fblog.exception.DBObjectExistsException;
import com.puyixiaowo.fblog.service.AfuTypeService;
import com.puyixiaowo.fblog.utils.*;
import com.puyixiaowo.fblog.utils.sign.SignUtils;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;

public class BaseController {
    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);


    /**
     * @param request
     * @param clazz
     * @param validate 是否校验参数
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends Validatable> T getParamsEntity(Request request,
                                                            Class<T> clazz,
                                                            boolean validate) throws ValidationException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Map<String, String[]> map = request.queryMap().toMap();

        T obj = (T) Class.forName(clazz.getName()).newInstance();
        BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
        BeanUtils.populate(obj, map);
        if (validate) {
            obj.validate();
        }

        Object id = ReflectionUtils.getFieldValue(obj, "id");

        if (id == null || id.equals(0L)) {
            ReflectionUtils.setFieldValue(obj, "id", null);
        }
        return obj;
    }

    public static <T> List<T> getParamsEntityJson(Request request,
                                                  Class<T> clazz,
                                                  boolean validate) {
        String json = request.queryParams("json");
        if (StringUtils.isBlank(json)) {
            throw new BaseControllerException("接收到的参数中不包含'json'");
        }

        JSONArray jsonArray = JSON.parseArray(json);

        return jsonArray.toJavaList(clazz);
    }

    public static Map<String, String> getParamsMap(Request request) {
        Map<String, String[]> map = request.queryMap().toMap();
        Map<String, String> mapReturn = new HashMap<>();
        for (Map.Entry entry : map.entrySet()) {
            String key = entry.getKey() == null ? "" : entry.getKey().toString();
            String value = entry.getValue() == null ? "" : ((String[]) entry.getValue())[0];
            mapReturn.put(key, value);
        }

        return mapReturn;
    }

    public static String renderHTML(String htmlFile) {
        try {
            // If you are using maven then your files
            // will be in a folder called resources.
            // getResource() gets that folder
            // and any files you specify.
            URL url = ResourceUtils.getResource(htmlFile);

            // Return a String which has all
            // the contents of the file.
            Path path = Paths.get(url.toURI());
            return new String(Files.readAllBytes(path), Charset.defaultCharset());
        } catch (IOException | URISyntaxException e) {
            // Add your own exception handlers here.
        }
        return null;
    }

    /**
     * 获取分页RowBouds
     *
     * @param request
     * @return
     */
    public static PageBean getPageBean(Request request) {

        String pageCurrentStr = request.queryParams("pageCurrent");
        String pageSizeStr = request.queryParams("pageSize");
        String order = request.queryParamOrDefault("order", "id");
        Boolean reverse = Boolean.valueOf(request.queryParamOrDefault("reverse", "true"));

        int pageCurrent = 1;
        int pageSize = Constants.DEFAULT_PAGE_SIZE;
        if (StringUtils.isNotBlank(pageCurrentStr)) {
            pageCurrent = Integer.valueOf(pageCurrentStr);
        }
        if (StringUtils.isNotBlank(pageSizeStr)) {
            pageSize = Integer.valueOf(pageSizeStr);
        }
        return new PageBean(pageCurrent, pageSize, order, reverse);
    }


    /**
     * 验签
     *
     * @param request
     * @return
     */
    public static boolean verifySign(Request request) {

        String idStr = request.queryParams("type");

        if (StringUtils.isBlank(idStr)) {
            return false;
        }

        AfuTypeBean afuTypeBean = null;
        try {
            afuTypeBean = AfuTypeService.getAfuTypeById(Long.valueOf(idStr));
        } catch (Exception e) {
            return false;
        }

        if (afuTypeBean == null ||
                StringUtils.isBlank(afuTypeBean.getPublicKey())) {
            return false;
        }
        String sign = request.queryParams("sign");
        Map<String, String> params = getParamsMap(request);
        return SignUtils.verify(params, sign, afuTypeBean.getPublicKey());
    }


    public static void saveAccessRecord(Request request,
                                        Long articleId) {
        ScheduledExecutorService exec = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("saveAccessRecord-schedule-pool-%d")
                        .daemon(true).build());

        FutureTask futureTask = new FutureTask(() -> {

            String link = request.uri()
                    + (StringUtils.isNotBlank(request.queryString())
                    ? "?" + request.queryString() : "");

            String userAgentString = request.headers("user-agent");
            UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);
            String os = userAgent
                    .getOperatingSystem().getName();

            String browser = userAgent.getBrowser().getName()
                            + " " + userAgent.getBrowserVersion();

            AccessRecordBean accessRecordBean = new AccessRecordBean();

            accessRecordBean.setArticleId(articleId);
            accessRecordBean.setLink(link);
            accessRecordBean.setIp(IpUtils.getIp(request));
            accessRecordBean.setAccessDate(DateUtils.getTodayZeroMiliseconds());
            accessRecordBean.setCreateDate(System.currentTimeMillis());
            accessRecordBean.setUserAgent(userAgentString);
            accessRecordBean.setOs(os);
            accessRecordBean.setBrowser(browser);


            try {
                DBUtils.insertOrUpdate(accessRecordBean, false);
            } catch (DBObjectExistsException e) {
            } catch (Exception e) {
                logger.error("[保存访问记录异常]:" + e.getMessage());
            }
            return null;
        });
        exec.submit(futureTask);
        exec.shutdown();
    }

    public static void main(String[] args) {
        System.out.println(DateFormatUtils.format(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000), "yyyy-MM-dd HH:mm:ss", Locale.CHINA));
    }
}
