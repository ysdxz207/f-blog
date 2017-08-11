package com.puyixiaowo.fblog.Controller;

import com.puyixiaowo.core.entity.Validatable;
import com.puyixiaowo.core.exceptions.ValidationException;
import org.apache.commons.beanutils.BeanUtils;
import spark.Request;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class BaseController {

    /**
     *
     * @param request
     * @param clazz
     * @param validate
     *          是否校验参数
     * @param <T>
     * @return
     */
    public static <T extends Validatable> T getParamEntity(Request request,
                                                           Class clazz,
                                                           boolean validate) throws ValidationException {
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
        obj.validate();
        return obj;
    }
}
