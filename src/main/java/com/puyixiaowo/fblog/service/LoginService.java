package com.puyixiaowo.fblog.service;

import com.puyixiaowo.fblog.bean.admin.UserBean;
import com.puyixiaowo.fblog.constants.Constants;
import com.puyixiaowo.fblog.error.LoginError;
import win.hupubao.common.error.Throws;
import win.hupubao.common.utils.DesUtils;
import win.hupubao.common.utils.StringUtils;

import java.util.Map;

/**
 * @author W.feihong
 * @date 2017-08-06
 */
public class LoginService {

    public static UserBean login(UserBean userBean) throws Exception{

        if (StringUtils.isBlank(userBean.getCaptcha())) {
            Throws.throwError(LoginError.EMPTY_CAPTCHA_ERROR);
        }


        if (!userBean.getCaptcha().equalsIgnoreCase(userBean.getSessionCaptcha())) {
            Throws.throwError(LoginError.WRONG_CAPTCHA_ERROR);
        }

        userBean.setPassword(DesUtils.encrypt(userBean.getPassword(),
                Constants.PASS_DES_KEY));

        userBean = userBean.selectOne("SELECT\n" +
                "  u.*,\n" +
                "  r.id AS role_id\n" +
                "FROM user u\n" +
                "  LEFT JOIN user_role ur\n" +
                "    ON u.id = ur.user_id\n" +
                "  LEFT JOIN role r\n" +
                "    ON r.id = ur.role_id\n" +
                "WHERE loginname = :loginname\n" +
                "      AND password = :password\n" +
                "      AND status = 1;");

        if (userBean == null) {
            Throws.throwError(LoginError.WRONG_USERNAME_OR_PASSWORD_ERROR);
        }
        return userBean;
    }

    public static UserBean cookieLogin(Map<String, String> cookies,
                                       String sessionCaptcha) throws Exception{

        UserBean userBean = null;
        String str = cookies.get(Constants.COOKIE_LOGIN_KEY_FBLOG);

        if (StringUtils.isNotBlank(str)) {
            userBean = new UserBean();
            String[] strArr = DesUtils.decrypt(str, Constants.PASS_DES_KEY).split("_");
            userBean.setLoginname(strArr[0]);
            userBean.setPassword(strArr[1]);
            userBean.setCaptcha(sessionCaptcha);
            userBean.setSessionCaptcha(sessionCaptcha);
            userBean = login(userBean);
        }


        return userBean;
    }
}
