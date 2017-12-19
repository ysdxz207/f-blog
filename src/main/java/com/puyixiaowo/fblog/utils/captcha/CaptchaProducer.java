package com.puyixiaowo.fblog.utils.captcha;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.puyixiaowo.fblog.enums.EnumCaptchaType;
import com.puyixiaowo.fblog.utils.ResourceUtils;

/**
 * 
 * @author Moses
 * @date 2017-12-05
 * 
 */
public class CaptchaProducer extends DefaultKaptcha {

    private static final String PATH_CAPTCHA_ADMIN = "conf/captcha_admin.properties";
    private static final String PATH_CAPTCHA_BOOK = "conf/captcha_book.properties";
    private Config config;

    public CaptchaProducer(EnumCaptchaType enumCaptchaType) {

        String url = PATH_CAPTCHA_ADMIN;
        switch (enumCaptchaType) {
            case CAPTCHA_TYPE_ADMIN:
                url = PATH_CAPTCHA_ADMIN;
                break;
            case CAPTCHA_TYPE_BOOK:
                url = PATH_CAPTCHA_BOOK;
                break;
        }
        this.config = new Config(ResourceUtils.load(url));
    }

    @Override
    public Config getConfig() {
        return this.config;
    }
}
