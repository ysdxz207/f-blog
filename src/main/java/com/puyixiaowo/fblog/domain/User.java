package com.puyixiaowo.fblog.domain;

import com.puyixiaowo.core.entity.Validatable;
import com.puyixiaowo.fblog.annotation.Id;
import com.puyixiaowo.fblog.annotation.NotNull;
import com.puyixiaowo.fblog.annotation.Transient;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Moses
 * @date 2017-08-04 9:32
 */
public class User extends Validatable implements Serializable{

    private static final long serialVersionUID = -6467427077007818085L;

    @Id
    private Long id;
    @NotNull(message = "用户名不可为空")
    private String loginname;
    @NotNull(message = "密码不可为空")
    private String nickname;
    private String password;
    private Date createTime;
    private Date lastLoginTime;
    private Boolean status;

    //////////////////
    @Transient
    private Long roleId;
    @Transient
    private String roleName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
