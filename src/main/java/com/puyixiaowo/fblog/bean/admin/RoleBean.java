package com.puyixiaowo.fblog.bean.admin;

import com.puyixiaowo.core.entity.Model;
import com.puyixiaowo.fblog.annotation.NotNull;
import com.puyixiaowo.fblog.annotation.Table;

@Table("role")
public class RoleBean extends Model<RoleBean> {
	private static final long serialVersionUID = 1L;

	private String id;
	@NotNull(message = "角色名不可为空")
	private String roleName;
	@NotNull(message = "角色代码不可为空")
	private String code;


	public String getId (){
		return id;
	}

	public void setId (String id){
		this.id = id;
	}

	public String getRoleName (){
		return roleName;
	}

	public void setRoleName (String roleName){
		this.roleName = roleName;
	}

	public String getCode (){
		return code;
	}

	public void setCode (String code){
		this.code = code;
	}
}