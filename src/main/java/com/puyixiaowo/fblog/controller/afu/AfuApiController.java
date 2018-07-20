package com.puyixiaowo.fblog.controller.afu;

import com.puyixiaowo.fblog.bean.admin.afu.AfuBean;
import com.puyixiaowo.fblog.bean.admin.afu.AfuTypeBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.bean.sys.ResponseBean;
import com.puyixiaowo.fblog.controller.BaseController;
import com.puyixiaowo.fblog.exception.db.DBObjectExistsException;
import com.puyixiaowo.fblog.service.AfuService;
import com.puyixiaowo.fblog.service.AfuTypeService;
import spark.Request;
import spark.Response;
import win.hupubao.common.utils.StringUtils;

/**
 * @author Moses
 * @date 2017-08-26
 */
public class AfuApiController extends BaseController {

    public static String apiAfus(Request request, Response response) {

        PageBean<AfuBean> pageBean = getPageBean(request);

        //验签
        if (!verifySign(request)) {
            pageBean.errorMessage("验签失败");
            return pageBean.serialize();
        }

        try {
            AfuBean afuBean = getParamsEntity(request, AfuBean.class, false);
            pageBean = AfuService.selectAfuPageBean(afuBean, pageBean);
            pageBean.success();
        } catch (Exception e) {
            pageBean.error(e);
        }
        return pageBean.serialize();
    }

    public static String apiAfusEdit(Request request, Response response) {

        ResponseBean responseBean = new ResponseBean();

        //验签
        if (!verifySign(request)) {
            responseBean.errorMessage("验签失败");
            return responseBean.serialize();
        }

        try {
            AfuBean afuBean = getParamsEntity(request, AfuBean.class, true);

            //可以根据名称修改
            AfuBean afuBeanDB = afuBean.selectOne("select * from afu where name=:name");

            if (afuBeanDB != null) {
                afuBeanDB.setContent(afuBean.getContent());
                afuBean = afuBeanDB;
            }

            //类别
            AfuTypeBean afuTypeBean = AfuTypeService.getAfuTypeById(afuBean.getType());

            if (afuTypeBean == null) {
                responseBean.errorMessage("类别不存在");
            } else if (afuTypeBean.getStatus() == 0) {
                responseBean.errorMessage("当前类别已失效");
            } else {
                afuBean.setType(afuTypeBean.getId());
                afuBean.setCreateTime(System.currentTimeMillis());
                afuBean.insertOrUpdate(false);
            }
            responseBean.success();
        } catch (DBObjectExistsException e) {
            responseBean.errorMessage("当前类别下已存在此阿福");
        } catch (Exception e) {
            responseBean.error(e);
        }

        return responseBean.serialize();
    }

    public static String apiAfusDelete(Request request, Response response) {

        ResponseBean responseBean = new ResponseBean();

        //验签
        if (!verifySign(request)) {
            responseBean.errorMessage("验签失败");
            return responseBean.serialize();
        }

        String id = request.queryParams("id");
        String name = request.queryParams("name");
        String typeStr = request.queryParams("type");
        String type = StringUtils.isBlank(typeStr) ? "0" : typeStr;
        try {
                AfuBean afuBean = new AfuBean();
            if (StringUtils.isNotBlank(id)) {
                afuBean.setId(id);
                afuBean.delete();
            } else if (StringUtils.isNotBlank(name)
                    && StringUtils.isNotBlank(type)){
                afuBean.setName(name);
                afuBean.setType(type);
                afuBean.deleteOrUpdate("delete from afu where name = :name and type=:type");
            }
            responseBean.success();
        } catch (Exception e) {
            responseBean.error(e);
        }

        return responseBean.serialize();
    }
}
