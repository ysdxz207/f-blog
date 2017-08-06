package com.puyixiaowo.fblog.filters;

import com.puyixiaowo.fblog.Constants.Constants;
import com.puyixiaowo.fblog.utils.ResourceUtils;
import org.yaml.snakeyaml.Yaml;

import java.util.List;
import java.util.Map;


import static spark.Spark.before;
/**
 *
 * @author feihong
 * @date 2017-08-06 18:04:00
 * 管理后台用户权限控制过滤器
 */
public class AdminAuthFilter {

    private static final String ADMIN_CONFIG_FILE = "conf/admin_auth.yaml";
    private static final String IGNORE_LIST = "ignore_list";

    public static void init() {
        //后台管理
        before((request, response) -> {
            String uri = request.uri();
            if (uri.startsWith("/admin")
                    && !isIgnorePath(uri)
                    && (request.session().attribute(Constants.SESSION_USER_KEY) == null))
                response.redirect("/admin/loginPage");
        });
    }

    private static boolean isIgnorePath(String uri) {


        Yaml yaml = new Yaml();
        Object obj = yaml.load(ResourceUtils.readFile(ADMIN_CONFIG_FILE));

        if (!(obj instanceof Map)) {
            throw new RuntimeException("后台用户权限配置不正确");
        }
        Map<String, List> map = (Map) obj;

        List<String> ignores = map.get(IGNORE_LIST);

        if (ignores == null) {
            throw new RuntimeException("后台用户权限配置不正确");
        }

        for (String path : ignores) {
            if (removeFirstSeparator(path).equals(removeFirstSeparator(uri))) {
                return true;
            }
        }
        return false;
    }

    private static String removeFirstSeparator(String path) {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        return path;
    }

}
