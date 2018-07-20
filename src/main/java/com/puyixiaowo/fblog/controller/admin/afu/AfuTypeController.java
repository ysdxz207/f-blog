package com.puyixiaowo.fblog.controller.admin.afu;

import com.puyixiaowo.fblog.annotation.admin.RequiresPermissions;
import com.puyixiaowo.fblog.bean.admin.afu.AfuBean;
import com.puyixiaowo.fblog.bean.admin.afu.AfuTypeBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.bean.sys.ResponseBean;
import com.puyixiaowo.fblog.controller.BaseController;
import com.puyixiaowo.fblog.service.AfuTypeService;
import spark.Request;
import spark.Response;
import win.hupubao.common.utils.StringUtils;
import win.hupubao.common.utils.rsa.RSA;

/**
 * @author Moses
 * @date 2017-09-05
 */
public class AfuTypeController extends BaseController {

    @RequiresPermissions(value = {"afuType:view"})
    public static String afuTypes(Request request, Response response) {
        PageBean<AfuTypeBean> pageBean = getPageBean(request);

        try {
            pageBean = AfuTypeService.selectAfuTypePageBean(
                    getParamsEntity(request, AfuTypeBean.class, false), pageBean);
            pageBean.success();
        } catch (Exception e) {
            pageBean.error(e);
        }
        return pageBean.serialize();
    }

    @RequiresPermissions(value = {"afuType:edit"})
    public static String detail(Request request, Response response) {

        ResponseBean responseBean = new ResponseBean();
        try {
            AfuTypeBean afuTypeBean = getParamsEntity(request, AfuTypeBean.class, false);
            if (afuTypeBean.getId() == null) {
                responseBean.errorMessage("id不可为空");
            }
            afuTypeBean = afuTypeBean.selectOne("select * from afu_type where id=:id");
            responseBean.success(afuTypeBean);
        } catch (Exception e) {
            responseBean.error(e);
        }
        return responseBean.serialize();
    }


    @RequiresPermissions(value = {"afuType:edit"})
    public static String edit(Request request, Response response) {

        ResponseBean responseBean = new ResponseBean();
        try {
            AfuTypeBean afuTypeBean = getParamsEntity(request, AfuTypeBean.class, false);
            if (afuTypeBean.getId() == null) {
                RSA.RSAKey key = new RSA().generateRSAKey();
                afuTypeBean.setPrivateKey(StringUtils.replaceBlank(key.getPrivateKey()));
                afuTypeBean.setPublicKey(StringUtils.replaceBlank(key.getPublicKey()));
            }
            afuTypeBean.insertOrUpdate(false);
            responseBean.success();
        } catch (Exception e) {
            responseBean.error(e);
        }
        return responseBean.serialize();
    }

    @RequiresPermissions(value = {"afuType:delete"})
    public static String delete(Request request, Response response) {
        ResponseBean responseBean = new ResponseBean();

        try {
            String ids = request.queryParams("id");
            new AfuTypeBean().deleteByIds(ids.split(","));
            //删除对应阿福
            AfuBean afuBean = new AfuBean();
            for (String id : ids.split(",")) {
                afuBean.setType(id);
                afuBean.deleteOrUpdate("delete from afu where type=:type");
            }

            responseBean.success();
        } catch (Exception e) {
            responseBean.error(e);
        }

        return responseBean.serialize();
    }

}
