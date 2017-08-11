package com.puyixiaowo.fblog.bean;

import com.alibaba.fastjson.JSON;
import org.eclipse.jetty.http.HttpStatus;

/**
 * @author feihong
 * @date 2017-08-06 21:48
 */
public class ResponseBean {
    private Integer status;
    private String message;
    private Boolean success;
    private Object data;


    public ResponseBean() {
        error("失败");
    }

    public void error(String message){
        this.status = HttpStatus.MULTIPLE_CHOICES_300;
        this.message = message;
        this.success = false;
    }

    public void success(){
        success("成功");
    }

    public void success(String message){
        this.status = HttpStatus.OK_200;
        this.message = message;
        this.success = true;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }


    //////////////////////////////

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
