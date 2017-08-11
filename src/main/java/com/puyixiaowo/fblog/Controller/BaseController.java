package com.puyixiaowo.fblog.Controller;

import org.apache.commons.beanutils.BeanUtils;
import spark.Request;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class BaseController {

    public static <T> T getParamEntity(Request request, Class clazz){
        Map<String, String[]> map = request.queryMap().toMap();
        T obj = null;
        try {
            obj = (T) Class.forName(clazz.getName()).newInstance();
            BeanUtils.populate(obj, map);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
