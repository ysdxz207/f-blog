package com.puyixiaowo.fblog.Controller.admin;

import com.puyixiaowo.fblog.domain.User;
import com.puyixiaowo.fblog.utils.DBUtils;
import spark.Request;
import spark.Response; /**
 * @author Moses
 * @date 2017-08-10 9:57
 */
public class AdminController {


    public static Object editUser(Request request, Response response) {
        User user = new User();
        user.setId("111");
        user.setPassword("1122");
        user.setUsername("test");
        user.setNickname("测试帐号");

        return DBUtils.insertOrUpdate(user);
    }
}
