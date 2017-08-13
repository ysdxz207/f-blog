package com.puyixiaowo.fblog.domain;

import com.puyixiaowo.core.entity.Validatable;

import java.io.Serializable;

/**
 * @author feihong
 * @date 2017-08-13 12:15
 */
public class Role extends Validatable implements Serializable{
    private static final long serialVersionUID = 1228159077219494748L;

    private Long id;
    private String roleName;
    private String code;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
