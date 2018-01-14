package com.puyixiaowo.fblog.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.Producer;
import com.puyixiaowo.fblog.bean.admin.UserBean;
import com.puyixiaowo.fblog.bean.admin.UserRoleBean;
import com.puyixiaowo.fblog.bean.sys.ResponseBean;
import com.puyixiaowo.fblog.constants.Constants;
import com.puyixiaowo.fblog.controller.BaseController;
import com.puyixiaowo.fblog.enums.EnumLoginType;
import com.puyixiaowo.fblog.freemarker.FreeMarkerTemplateEngine;
import com.puyixiaowo.fblog.service.LoginService;
import com.puyixiaowo.fblog.utils.DesUtils;
import com.puyixiaowo.fblog.utils.StringUtils;
import com.puyixiaowo.fblog.utils.captcha.CaptchaProducer;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Moses
 * @date 2017-08-08
 * 登录
 */
public class LoginController extends BaseController {

    private static Producer captchaProducerAdmin = new CaptchaProducer(EnumLoginType.LOGIN_TYPE_ADMIN);
    private static Producer captchaProducerBook= new CaptchaProducer(EnumLoginType.LOGIN_TYPE_BOOK);

    /**
     * 登录页面
     *
     * @param request
     * @param response
     * @return
     */
    public static Object loginPage(Request request, Response response) {


        return new FreeMarkerTemplateEngine()
                .render(new ModelAndView(null, "admin/login.html"));
    }

    /**
     * 管理后台登录
     *
     * @param request
     * @param response
     * @return
     */
    public static Object adminLogin(Request request,
                                       Response response) {

        return login(Constants.COOKIE_LOGIN_KEY_FBLOG,
                request, response);
    }

    /**
     * 书登录页面
     *
     * @param request
     * @param response
     * @return
     */
    public static Object loginPageBook(Request request, Response response) {

        return new FreeMarkerTemplateEngine()
                .render(new ModelAndView(null, "tools/book/book_login.html"));
    }

    /**
     * 书登录
     *
     * @param request
     * @param response
     * @return
     */
    public static Object bookLogin(Request request,
                                          Response response) {

        return login(Constants.COOKIE_LOGIN_KEY_BOOK,
                request, response);
    }

    public static ResponseBean login(String cookieKey,
                                     Request request,
                                     Response response) {

        ResponseBean responseBean = new ResponseBean();

        String captcha = request.queryParams("captcha");
        if (StringUtils.isBlank(captcha)) {
            responseBean.errorMessage("请输入验证码");
            return responseBean;
        }


        String sessionCaptcha = request.session().attribute(Constants.KAPTCHA_SESSION_KEY);
        if (!captcha.equalsIgnoreCase(sessionCaptcha)) {
            responseBean.errorMessage("验证码错误");
            return responseBean;
        }

        String uname = request.queryParams("uname");
        String upass = request.queryParams("upass");

        if (StringUtils.isBlank(uname)) {
            responseBean.errorMessage("用户名为空");
            return responseBean;
        }
        if (StringUtils.isBlank(upass)) {
            responseBean.errorMessage("密码为空");
            return responseBean;
        }
        return doLogin(cookieKey,
                uname, DesUtils.encrypt(upass), request, response);
    }

    private static ResponseBean doLogin(String cookieKey,
                                        String uname,
                                String upassEncrypt,
                                Request request,
                                Response response) {

        ResponseBean responseBean = new ResponseBean();

        Map<String, Object> params = new HashMap<>();

        params.put("loginname", uname);
        params.put("password", upassEncrypt);

        try {
            UserBean userBean = LoginService.login(params);
            if (userBean == null) {
                responseBean.errorMessage("用户名或密码不正确");
                return responseBean;
            } else {
                //登录成功
                request.session().attribute(Constants.SESSION_USER_KEY, userBean);
                rememberMe(cookieKey, request, response, userBean);
                return responseBean;
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseBean.errorMessage("登录异常：" + e.getMessage());
            return responseBean;
        }
    }

    public static UserBean rememberMe(String cookieKey,
                                      Request request,
                                   Response response,
                                   UserBean userBean) {
        String rememberMeStr = request.queryParams("rememberMe");

        boolean rememberMe = StringUtils.isNotBlank(rememberMeStr)
                && Boolean.valueOf(rememberMeStr);

        if (userBean != null) {
            if (!rememberMe) {
                return null;
            }
            String cookieStr = userBean.getLoginname() + "_" + userBean.getPassword();
            response.cookie(cookieKey,
                    DesUtils.encrypt(cookieStr), 24*3600*365);
            return userBean;
        }

        userBean = new UserBean();
        String str = request.cookie(cookieKey);

        if (StringUtils.isNotBlank(str)) {
            String [] strArr = DesUtils.decrypt(str).split("_");
            userBean.setLoginname(strArr[0]);
            userBean.setPassword(strArr[1]);
            return userBean;
        } else {
            String uname = request.queryParams("uname");
            String upass = request.queryParams("upass");
            if (StringUtils.isBlank(uname)
                    && StringUtils.isBlank(upass)) {
                return null;
            }
            userBean.setLoginname(uname);
            if (StringUtils.isNotBlank(upass)) {
                userBean.setPassword(DesUtils.encrypt(upass));
            }
        }
        return userBean;
    }

    /**
     * 退出登录
     *
     * @param request
     * @param response
     * @return
     */
    public static Object logout(Request request, Response response) {
        ResponseBean responseBean = new ResponseBean();
        request.session().removeAttribute(Constants.SESSION_USER_KEY);

        String uri = request.uri();

        if (uri.toLowerCase().startsWith("/book")) {
            response.removeCookie("/book", Constants.COOKIE_LOGIN_KEY_BOOK);
        } else {
            response.removeCookie("/admin", Constants.COOKIE_LOGIN_KEY_FBLOG);
        }
        return responseBean;
    }

    /**
     * 验证码
     * @param request
     * @param response
     */
    public static Object captcha(Request request,
                        Response response) {

        String type = request.queryParamOrDefault("type", "admin");

        EnumLoginType enumLoginType = EnumLoginType.getEnumType(type);
        Producer producer = captchaProducerAdmin;

        switch (enumLoginType) {
            case LOGIN_TYPE_BOOK:
                producer = captchaProducerBook;
                break;
        }

        HttpSession session = request.session().raw();
        HttpServletResponse res = response.raw();

        res.setDateHeader("Expires", 0);

        // Set standard HTTP/1.1 no-cache headers.
        res.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        res.addHeader("Cache-Control", "post-check=0, pre-check=0");

        // Set standard HTTP/1.0 no-cache header.
        res.setHeader("Pragma", "no-cache");

        // return a jpeg
        res.setContentType("image/jpeg");



        // create the text for the image
        String capText = producer.createText();

        // store the text in the session
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);

        // create the image with the text
        ServletOutputStream out = null;
        try {
            BufferedImage bi = producer.createImage(capText);
            out = res.getOutputStream();
            // write the data out
            ImageIO.write(bi, "jpg", out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    public static void rememberMeLogin(String cookieKey,
                                       Request request,
                                       Response response) {
        String uri = request.uri();

        String redirectPage = "/admin/";
        String loginPage = "/admin/loginPage";
        if (uri.toLowerCase().startsWith("/book")) {
            if (!uri.equalsIgnoreCase("/book/login")) {
                String params = request.queryString();

                if (StringUtils.isBlank(params)) {
                    params = "";
                } else {
                    params = "?" + params;
                }
                redirectPage = request.url() + params;
            } else {
                redirectPage = "/book/index";
            }
            loginPage = "/book/loginPage";
        }

        UserBean userBean = rememberMe(cookieKey,
                request, response, null);

        if (userBean == null) {
            response.redirect(loginPage);
            return;
        }

        ResponseBean responseBean = doLogin(cookieKey,
                userBean.getLoginname(), userBean.getPassword(), request, response);

        if (responseBean.getStatusCode() == Constants.RESPONSE_STATUS_CODE_SUCCESS) {
            response.redirect(redirectPage);
        } else {
            response.redirect(loginPage);
            return;
        }
    }
}
