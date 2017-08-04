package com.puyixiaowo.fblog.domain;

import java.io.Serializable;

/**
 * @author Moses
 * @date 2017-08-04 9:32
 */
public class User implements Serializable{

    private static final long serialVersionUID = -6467427077007818085L;

    private String username;
    private String password;
    private String nickname;


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
