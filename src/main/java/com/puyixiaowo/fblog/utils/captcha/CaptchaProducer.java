package com.puyixiaowo.fblog.utils.captcha;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.puyixiaowo.fblog.utils.ResourceUtils;

/**
 * 
 * @author Moses
 * @date 2017-12-05
 * 
 */
public class CaptchaProducer extends DefaultKaptcha {

    private static final String PATH_CAPTCHA_ADMIN = "conf/captcha_admin.properties";
    private Config config;

    public CaptchaProducer() {

        this.config = new Config(ResourceUtils.load(PATH_CAPTCHA_ADMIN));
    }

    @Override
    public Config getConfig() {
        return this.config;
    }
}
