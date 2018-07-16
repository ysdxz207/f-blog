package com.puyixiaowo.fblog.bean.admin;

import com.puyixiaowo.core.entity.Validatable;
import com.puyixiaowo.fblog.annotation.NotNull;
import com.puyixiaowo.fblog.annotation.Table;

import java.io.Serializable;
import java.util.List;

@Table("menu")
public class MenuBean extends Validatable implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	@NotNull
	private String menuName;
	private String icon;
	private String sort;
	@NotNull
	private String href;
	private Integer status;
	private String remark;
	private String type;
	@NotNull
	private String code;
	@NotNull
	private String pid;
	private Integer expand;

	///////////////////
	private List<MenuBean> menuBeanList;


	public String getId (){
		return id;
	}

	public void setId (String id){
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

	public Integer getStatus (){
		return status;
	}

	public void setStatus (Integer status){
		this.status = status;
	}

	public String getRemark (){
		return remark;
	}

	public void setRemark (String remark){
		this.remark = remark;
	}

	public String getType (){
		return type;
	}

	public void setType (String type){
		this.type = type;
	}

	public String getCode (){
		return code;
	}

	public void setCode (String code){
		this.code = code;
	}

	public String getPid (){
		return pid;
	}

	public void setPid (String pid){
		this.pid = pid;
	}

	public Integer getExpand (){
		return expand;
	}

	public void setExpand (Integer expand){
		this.expand = expand;
	}

	public List<MenuBean> getMenuBeanList() {
		return menuBeanList;
	}

	public void setMenuBeanList(List<MenuBean> menuBeanList) {
		this.menuBeanList = menuBeanList;
	}
}