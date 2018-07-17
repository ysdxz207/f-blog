package com.puyixiaowo.fblog.controller.admin;

import com.puyixiaowo.fblog.bean.admin.UserBean;
import com.puyixiaowo.fblog.bean.sys.ResponseBean;
import com.puyixiaowo.fblog.constants.Constants;
import com.puyixiaowo.fblog.controller.BaseController;
import com.puyixiaowo.fblog.error.LoginError;
import com.puyixiaowo.fblog.freemarker.FreeMarkerTemplateEngine;
import com.puyixiaowo.fblog.service.LoginService;
import com.puyixiaowo.fblog.utils.StringUtils;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import win.hupubao.common.annotations.LogReqResArgs;
import win.hupubao.common.utils.Captcha;
import win.hupubao.common.utils.DesUtils;
import win.hupubao.common.utils.LoggerUtils;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static spark.Spark.halt;

/**
 * @author Moses
 * @date 2017-08-08
 * 登录
 */
public class LoginController extends BaseController {

    private static final Captcha captcha = Captcha.getInstance()
            .height(40)
            .width(80);


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


    @LogReqResArgs
    public static ResponseBean login(Request request,
                                     Response response) {

        ResponseBean responseBean = new ResponseBean();

        try {
            UserBean userBean = getParamsEntity(request, UserBean.class, true);

            String sessionCaptcha = request.session().attribute(Constants.KAPTCHA_SESSION_KEY);
            userBean.setSessionCaptcha(sessionCaptcha);


            LoggerUtils.info("[{0}][登录验证码]：{1}[收到验证码]：{2}",
                    request.session().id(),
                    sessionCaptcha,
                    captcha);

            userBean = LoginService.login(userBean);
            //登录成功
            userBean.setToken(request.session().id());
            responseBean.success(userBean);
            request.session().attribute(Constants.SESSION_USER_KEY, userBean);
            rememberMe(Constants.COOKIE_LOGIN_KEY_FBLOG, request, response, userBean);
        } catch (Exception e) {
            responseBean.error(e);
        }

        return responseBean;
    }


    public static UserBean rememberMe(String cookieKey,
                                      Request request,
                                      Response response,
                                      UserBean userBean) {
        String rememberMeStr = request.queryParams("rememberMe");

        boolean rememberMe = StringUtils.isNotBlank(rememberMeStr)
                && ("on".equalsIgnoreCase(rememberMeStr) || Boolean.valueOf(rememberMeStr));

        if (userBean != null) {
            if (!rememberMe) {
                return null;
            }
            String cookieStr = userBean.getLoginname() + "_" + userBean.getPassword();
            response.cookie(cookieKey,
                    DesUtils.encrypt(cookieStr, Constants.PASS_DES_KEY), 24 * 3600 * 365);
            return userBean;
        }

        userBean = new UserBean();
        String str = request.cookie(cookieKey);

        if (StringUtils.isNotBlank(str)) {
            String[] strArr = DesUtils.decrypt(str, Constants.PASS_DES_KEY).split("_");
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
                userBean.setPassword(DesUtils.encrypt(upass, Constants.PASS_DES_KEY));
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

        response.removeCookie("/admin", Constants.COOKIE_LOGIN_KEY_FBLOG);
        return responseBean;
    }

    /**
     * 验证码
     *
     * @param request
     * @param response
     */
    public static Object captcha(Request request,
                                 Response response) {
        ResponseBean responseBean = new ResponseBean();
        HttpServletResponse res = response.raw();

        res.setDateHeader("Expires", 0);

        // Set standard HTTP/1.1 no-cache headers.
        res.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        res.addHeader("Cache-Control", "post-check=0, pre-check=0");

        // Set standard HTTP/1.0 no-cache header.
        res.setHeader("Pragma", "no-cache");

        // return a jpeg
//        res.setContentType("image/jpeg");

        Captcha.CaptchaImage captchaImage = generateCaptcha(request);
        responseBean.success(captchaImage.getBase64Image());
        return responseBean;
    }

    private static Captcha.CaptchaImage generateCaptcha(Request request) {
        Captcha.CaptchaImage captchaImage = captcha.generate();
        HttpSession session = request.session().raw();

        LoggerUtils.info("[{0}][生成验证码]：{1}", session.getId(), captchaImage.getCaptchaCode());

        // store the text in the session
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, captchaImage.getCaptchaCode());

        return captchaImage;
    }

    public static void cookieLogin(Request request, Response response) {
        ResponseBean responseBean = new ResponseBean();
        try {
            //生成验证码
            generateCaptcha(request);
            UserBean userBean = LoginService.cookieLogin(request.cookies(), request.session().attribute(Constants.KAPTCHA_SESSION_KEY));
            if (userBean == null) {
                responseBean.error(LoginError.NO_AUTH_ERROR);
            } else {
                responseBean.success(userBean);
            }
        } catch (Exception e) {
            responseBean.error(e);
        }

        halt(responseBean.serialize());
    }
}
