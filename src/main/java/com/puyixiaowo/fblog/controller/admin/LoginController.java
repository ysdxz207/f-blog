package com.puyixiaowo.fblog.controller.admin;

import com.google.code.kaptcha.Producer;
import com.puyixiaowo.fblog.bean.admin.UserBean;
import com.puyixiaowo.fblog.constants.Constants;
import com.puyixiaowo.fblog.controller.BaseController;
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

    private static Producer captchaProducer = new CaptchaProducer();

    /**
     * 登录页面
     *
     * @param request
     * @param response
     * @return
     */
    public static ModelAndView loginPage(Request request, Response response) {
        return new ModelAndView(null, "admin/login.html");
    }

    /**
     * 管理后台登录
     *
     * @param request
     * @param response
     * @return
     */
    public static ModelAndView adminLogin(Request request,
                                       Response response) {

        String loginPage = "admin/login.html";
        String redirectPage = "/admin/";
        return login(request, response, redirectPage, loginPage);
    }

    /**
     * 书登录
     *
     * @param request
     * @param response
     * @return
     */
    public static ModelAndView bookLogin(Request request,
                                          Response response) {

        String loginPage = "book/login.html";
        String redirectPage = "/book";
        return login(request, response, redirectPage, loginPage);
    }

    public static ModelAndView login(Request request,
                                Response response,
                                String redirectPage,
                                String loginPage) {
        Map<String, Object> model = new HashMap<>();

        String captcha = request.queryParams("captcha");
        if (StringUtils.isBlank(captcha)) {
            model.put("message", "请输入验证码");
            return new ModelAndView(model, loginPage);
        }


        String sessionCaptcha = request.session().attribute(Constants.KAPTCHA_SESSION_KEY);
        if (!captcha.equalsIgnoreCase(sessionCaptcha)) {
            model.put("message", "验证码错误");
            return new ModelAndView(model, loginPage);
        }

        Map<String, Object> params = new HashMap<>();

        params.put("loginname", request.queryParams("uname"));
        params.put("password", DesUtils.encrypt(request.queryParams("upass")));

        UserBean userBean = null;

        try {
            userBean = LoginService.login(params);
            if (userBean == null) {
                model.put("message", "用户名或密码不正确");
            } else {

                request.session().attribute(Constants.SESSION_USER_KEY, userBean);
                response.redirect(redirectPage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.put("message", e.getMessage());
        }


        return new ModelAndView(model, loginPage);
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
        String capText = captchaProducer.createText();

        // store the text in the session
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);

        // create the image with the text
        ServletOutputStream out = null;
        try {
            BufferedImage bi = captchaProducer.createImage(capText);
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
