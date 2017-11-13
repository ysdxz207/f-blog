package com.puyixiaowo.fblog.controller.afu;

import com.puyixiaowo.fblog.bean.admin.afu.AfuBean;
import com.puyixiaowo.fblog.bean.admin.afu.AfuTypeBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.bean.sys.ResponseBean;
import com.puyixiaowo.fblog.controller.BaseController;
import com.puyixiaowo.fblog.exception.DBObjectExistsException;
import com.puyixiaowo.fblog.service.AfuService;
import com.puyixiaowo.fblog.service.AfuTypeService;
import com.puyixiaowo.fblog.utils.DBUtils;
import com.puyixiaowo.fblog.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

/**
 * @author Moses
 * @date 2017-08-26 21:23:47
 */
public class AfuApiController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(AfuApiController.class);

    public static String apiAfus(Request request, Response response) {

        PageBean pageBean = getPageBean(request);

        //验签
        if (!verifySign(request)) {
            pageBean.errorMessage("验签失败");
            return pageBean.serialize();
        }

        AfuBean afuBean = null;
        try {
            afuBean = getParamsEntity(request, AfuBean.class, false);
            pageBean = AfuService.selectAfuPageBean(afuBean, pageBean);
        } catch (Exception e) {
            logger.error(e.getMessage());
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

        AfuBean afuBean = null;
        try {
            afuBean = getParamsEntity(request, AfuBean.class, true);

            //类别
            AfuTypeBean afuTypeBean = AfuTypeService.getAfuTypeById(afuBean.getType());

            if (afuTypeBean == null) {
                responseBean.errorMessage("类别不存在");
            } if (afuTypeBean.getStatus() == 0) {
                responseBean.errorMessage("当前类别已失效");
            } else {
                afuBean.setType(afuTypeBean.getId());
                afuBean.setCreateTime(System.currentTimeMillis());
                DBUtils.insertOrUpdate(afuBean);
            }
        } catch (DBObjectExistsException e) {
            responseBean.errorMessage("当前类别下已有存在此阿福");
        } catch (Exception e) {
            responseBean.errorMessage(e.getMessage());
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
        Long type = Long.valueOf(StringUtils.isBlank(typeStr) ? "0" : typeStr);
        try {
            if (StringUtils.isNotBlank(id)) {
                DBUtils.deleteByIds(AfuBean.class, id);
            } else if (StringUtils.isNotBlank(name) && type > 0){
                AfuBean afuBean = new AfuBean();
                afuBean.setName(name);
                afuBean.setType(type);
                DBUtils.executeSql("delete from afu where name = :name and type=:type", afuBean);
            }
        } catch (Exception e) {
            responseBean.errorMessage(e.getMessage());
        }

        return responseBean.serialize();
    }
}
