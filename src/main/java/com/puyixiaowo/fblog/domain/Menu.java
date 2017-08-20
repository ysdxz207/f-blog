package com.puyixiaowo.fblog.domain;

import java.io.Serializable;

public class Menu implements Serializable {
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
	private Long pid;
	private Boolean expand;


	public Long getId (){
		return id;
	}

	public void setId (Long id){
		this.id = id;
	}

	public String getMenuName (){
		return menuName;
	}

	public void setMenuName (String menuName){
		this.menuName = menuName;
	}

	public String getIcon (){
		return icon;
	}

	public void setIcon (String icon){
		this.icon = icon;
	}

	public String getSort (){
		return sort;
	}

	public void setSort (String sort){
		this.sort = sort;
	}

	public String getHref (){
		return href;
	}

	public void setHref (String href){
		this.href = href;
	}

	public Boolean getStatus (){
		return status;
	}

	public void setStatus (Boolean status){
		this.status = status;
	}

	public String getRemark (){
		return remark;
	}

	public void setRemark (String remark){
		this.remark = remark;
	}

	public Integer getType (){
		return type;
	}

	public void setType (Integer type){
		this.type = type;
	}

	public String getCode (){
		return code;
	}

	public void setCode (String code){
		this.code = code;
	}

	public Long getPid (){
		return pid;
	}

	public void setPid (Long pid){
		this.pid = pid;
	}

	public Boolean getExpand (){
		return expand;
	}

	public void setExpand (Boolean expand){
		this.expand = expand;
	}
}