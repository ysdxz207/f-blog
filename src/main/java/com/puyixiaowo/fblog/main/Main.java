package com.puyixiaowo.fblog.main;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.puyixiaowo.fblog.Routes;
import com.puyixiaowo.fblog.bean.sys.AppConfigBean;
import com.puyixiaowo.fblog.error.ErrorHandler;
import com.puyixiaowo.fblog.generator.utils.CustomIdSerializer;
import com.puyixiaowo.fblog.utils.AppUtils;
import com.puyixiaowo.fblog.utils.ConfigUtils;
import com.puyixiaowo.fblog.utils.DBUtils;

import static spark.Spark.port;

/**
 * @author Moses
 * @date 2017-08-01 18:21
 */
public class Main {
    /**
     * 支持启动设置端口：java -jar f-blog-1.0.jar -p 1521,
     * 默认启动端口8003
     * @param args
     */
    public static void main(String[] args) {

        AppConfigBean config = AppUtils.getAppConfigBean(args);
        port(config.getPort());

        ErrorHandler.handleErrors();
        ConfigUtils.init();
        DBUtils.initDB();
        Routes.init();

        //ID序列化为字符串类型
        SerializeConfig.getGlobalInstance().put(Long.class, new CustomIdSerializer());
    }
}
