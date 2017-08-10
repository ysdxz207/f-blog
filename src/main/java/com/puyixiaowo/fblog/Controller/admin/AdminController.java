package com.puyixiaowo.fblog.Controller.admin;

import com.puyixiaowo.fblog.domain.User;
import com.puyixiaowo.fblog.utils.DBUtils;
import com.puyixiaowo.fblog.utils.IdUtils;
import com.puyixiaowo.fblog.utils.Md5Utils;
import spark.Request;
import spark.Response; /**
 * @author Moses
 * @date 2017-08-10 9:57
 */
public class AdminController {


    public static Object editUser(Request request, Response response) {
        String username = request.queryParams("uname");
        String password = request.queryParams("upass");
        String nickname = request.queryParams("nname");

        

        User user = new User();
        user.setId(IdUtils.generateId());
        user.setPassword(Md5Utils.md5Password(password));
        user.setUsername(username);
        user.setNickname(nickname);

        return DBUtils.insertOrUpdate(user);
    }
}
