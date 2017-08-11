package com.puyixiaowo.fblog.domain;

import com.puyixiaowo.core.entity.Validatable;
import com.puyixiaowo.fblog.annotation.Id;
import com.puyixiaowo.fblog.annotation.NotNull;

import java.io.Serializable;

/**
 * @author Moses
 * @date 2017-08-04 9:32
 */
public class User extends Validatable implements Serializable{

    private static final long serialVersionUID = -6467427077007818085L;

    @Id
    private Long id;
    @NotNull(message = "用户名不可为空")
    private String username;
    @NotNull(message = "密码不可为空")
    private String password;
    private String nickname;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
