package com.puyixiaowo.fblog.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.Producer;
import com.puyixiaowo.fblog.bean.admin.UserBean;
import com.puyixiaowo.fblog.bean.admin.UserRoleBean;
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

        login(request, response);
        return null;
    }

    /**
     * 书登录页面
     *
     * @param request
     * @param response
     * @return
     */
    public static Object loginPageBook(Request request, Response response) {

        UserBean userBean = request.session().attribute(Constants.SESSION_USER_KEY);

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

        login(request, response);
        return null;
    }

    public static void login(Request request,
                               Response response) {

        String loginPage = "/admin/loginPage";
        String redirectPage = "/admin/";

        String uri = request.uri();

        if (uri.toLowerCase().startsWith("/book")) {
            loginPage = "/book/loginPage";
            redirectPage = "/book/index";
        }
        Map<String, Object> model = new HashMap<>();

        UserBean userBean = rememberMe(request, response, null);

        if (userBean == null) {
            response.redirect(loginPage);
            return;
        }

        String captcha = request.queryParams("captcha");
//            if (StringUtils.isBlank(captcha)) {
//                model.put("message", "请输入验证码");
//                response.redirect(loginPage);
//                return;
//            }
//
//
//            String sessionCaptcha = request.session().attribute(Constants.KAPTCHA_SESSION_KEY);
//            if (!captcha.equalsIgnoreCase(sessionCaptcha)) {
//                model.put("message", "验证码错误");
//                response.redirect(loginPage);
//                return;
//            }

        if (StringUtils.isBlank(userBean.getLoginname())) {
            model.put("message", "用户名为空");
            return;
        }
        if (StringUtils.isBlank(userBean.getPassword())) {
            model.put("message", "密码为空");
            return;
        }
        Map<String, Object> params = new HashMap<>();

        params.put("loginname", userBean.getLoginname());
        params.put("password", userBean.getPassword());

        try {
            userBean = LoginService.login(params);
            if (userBean == null) {
                model.put("message", "用户名或密码不正确");
            } else {
                //登录成功
                request.session().attribute(Constants.SESSION_USER_KEY, userBean);
                rememberMe(request, response, userBean);
                response.redirect(redirectPage);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.put("message", e.getMessage());
        }

        response.redirect(loginPage);
        return;
    }

    public static UserBean rememberMe(Request request,
                                   Response response,
                                   UserBean userBean) {

        if (userBean != null) {
            String cookieStr = userBean.getLoginname() + "_" + userBean.getPassword();
            response.cookie(Constants.COOKIE_LOGIN_KEY,
                    DesUtils.encrypt(cookieStr));
            return userBean;
        }

        userBean = new UserBean();
        String str = request.cookie(Constants.COOKIE_LOGIN_KEY);

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
        request.session().removeAttribute(Constants.SESSION_USER_KEY);

        response.redirect("/admin/loginPage");
        return "";
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
}
