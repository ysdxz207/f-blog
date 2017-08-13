package com.puyixiaowo.fblog.domain;

import com.puyixiaowo.core.entity.Validatable;

import java.io.Serializable;

/**
 * @author feihong
 * @date 2017-08-13 22:09
 */
public class Menu extends Validatable implements Serializable {

    private static final long serialVersionUID = 4273644926739258656L;

    private Long id;
    private String menuName;
    private String icon;
    private String sort;

}
