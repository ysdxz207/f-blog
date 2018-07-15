/**
 *
 */
package com.puyixiaowo.fblog.bean.sys;

import win.hupubao.common.beans.ResponseBase;

/**
 * @author huangfeihong
 * @date 2016年12月6日 下午9:24:25
 */
public class ResponseBean extends ResponseBase {

	private static final long serialVersionUID = -5266170746828998914L;

	//////////
	private boolean closeCurrent = true;//默认关闭当前对话框
	private String tabid;
	private String datagrids;
	private String forward;
	private String forwardConfirm;

	public boolean isCloseCurrent() {
		return closeCurrent;
	}

	public void setCloseCurrent(boolean closeCurrent) {
		this.closeCurrent = closeCurrent;
	}

	public String getTabid() {
		return tabid;
	}

	public void setTabid(String tabid) {
		this.tabid = tabid;
	}

	public String getDatagrids() {
		return datagrids;
	}

	public void setDatagrids(String datagrids) {
		this.datagrids = datagrids;
	}

	public String getForward() {
		return forward;
	}

	public void setForward(String forward) {
		this.forward = forward;
	}

	public String getForwardConfirm() {
		return forwardConfirm;
	}

	public void setForwardConfirm(String forwardConfirm) {
		this.forwardConfirm = forwardConfirm;
	}


	//////////////////////////////

}
