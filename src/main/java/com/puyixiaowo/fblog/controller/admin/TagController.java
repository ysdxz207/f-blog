package com.puyixiaowo.fblog.controller.admin;

import com.alibaba.fastjson.JSON;
import com.puyixiaowo.fblog.annotation.admin.RequiresPermissions;
import com.puyixiaowo.fblog.bean.admin.TagBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.bean.sys.ResponseBean;
import com.puyixiaowo.fblog.controller.BaseController;
import com.puyixiaowo.fblog.freemarker.FreeMarkerTemplateEngine;
import com.puyixiaowo.fblog.service.TagService;
import com.puyixiaowo.fblog.utils.DBUtils;
import com.puyixiaowo.fblog.utils.StringUtils;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Moses
 * @date 2017-09-05 22:31:38
 * 
 */
public class TagController extends BaseController {

    @RequiresPermissions(value = {"tag:view"})
    public static String tags(Request request, Response response) {
        Boolean data = Boolean.valueOf(request.params(":data"));

        if (!data) {
            return new FreeMarkerTemplateEngine()
                    .render(new ModelAndView(null,
                            "admin/tag/tag_list.html"));
        }
        PageBean pageBean = getPageBean(request);
        try {
            TagBean tagBean = getParamsEntity(request, TagBean.class, false);
            List<TagBean> list = TagService.selectTagList(tagBean,
                    pageBean);
            pageBean.setList(list);

            int count = TagService.selectCount(tagBean);
            pageBean.setTotalCount(count);
        } catch (Exception e) {
            pageBean.error(e);
        }
        return pageBean.serialize();
    }


    @RequiresPermissions(value = {"tag:edit"})
    public static String edit(Request request, Response response){
        ResponseBean responseBean = new ResponseBean();
        List<TagBean> tagBeanList = getParamsEntityJson(request, TagBean.class, true);
        try {

            for (TagBean tagBean :
                    tagBeanList) {
                DBUtils.insertOrUpdate(tagBean);
            }
        } catch (Exception e) {
            responseBean.error(e);
        }
        return responseBean.serialize();
    }

    @RequiresPermissions(value = {"tag:delete"})
    public static String delete(Request request, Response response){
        ResponseBean responseBean = new ResponseBean();

        try {
            DBUtils.deleteByIds(TagBean.class,
                    request.queryParams("id"));
        } catch (Exception e) {
            responseBean.error(e);
        }

        return responseBean.serialize();
    }

    @RequiresPermissions(value = {"tag:view"})
    public static String topArray(Request request) {

        String tagName = request.queryParams("tagName");
        Map<String, Object> params = new HashMap<>();
        String sql = "select t.name from tag t\n" +
                "  left join\n" +
                "(select at.*,count(at.id) as pop\n" +
                "from article_tag at\n" +
                "group by at.tag_id\n" +
                "order by pop desc) mt\n" +
                "on t.id = mt.tag_id where 1 = 1 ";

        if (StringUtils.isNotBlank(tagName)) {
            sql += " and name like :tagName ";
            params.put("tagName", "%" + request.queryParams("tagName") + "%");

        }

        sql += "limit 10";
        List<String> list = DBUtils.selectList(String.class,
                sql, params);

        return JSON.toJSONString(list);
    }

}
