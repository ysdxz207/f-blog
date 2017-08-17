package com.puyixiaowo.fblog.domain;

import java.io.Serializable;

import com.puyixiaowo.core.entity.Validatable;

public class Menu extends Validatable implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String menuName;
	private String icon;
	private String sort;
	private String href;
	private Boolean status;
	private String remark;
	private Integer type;
	private String code;
	private String pid;
	private Boolean expand;


	public Long getId (){
		return id;
	}

	public void setId (Long id){
		id = id;
	}

	public String getMenuName (){
		return menuName;
	}

	public void setMenuName (String menuName){
		menuName = menuName;
	}

	public String getIcon (){
		return icon;
	}

	public void setIcon (String icon){
		icon = icon;
	}

	public String getSort (){
		return sort;
	}

	public void setSort (String sort){
		sort = sort;
	}

	public String getHref (){
		return href;
	}

	public void setHref (String href){
		href = href;
	}

	public Boolean getStatus (){
		return status;
	}

	public void setStatus (Boolean status){
		status = status;
	}

	public String getRemark (){
		return remark;
	}

	public void setRemark (String remark){
		remark = remark;
	}

	public Integer getType (){
		return type;
	}

	public void setType (Integer type){
		type = type;
	}

	public String getCode (){
		return code;
	}

	public void setCode (String code){
		code = code;
	}

	public String getPid (){
		return pid;
	}

	public void setPid (String pid){
		pid = pid;
	}

	public Boolean getExpand (){
		return expand;
	}

	public void setExpand (Boolean expand){
		expand = expand;
	}
}