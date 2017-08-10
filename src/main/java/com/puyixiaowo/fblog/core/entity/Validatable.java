package com.puyixiaowo.fblog.core.entity;

import com.puyixiaowo.fblog.annotation.NotNull;
import com.puyixiaowo.fblog.utils.StringUtils;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

/**
 * @author feihong
 * @date 2017-08-10 23:08
 */
public abstract class Validatable {

    public void validate(){
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        Field [] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            NotNull notnull = field.getAnnotation(NotNull.class);
            String fieldName = notnull.fieldName();
            String message = notnull.message();

            try {
                if (StringUtils.isBlank(field.get(this))) {

                }
            } catch (IllegalAccessException e) {

            }

        }
    }
}
