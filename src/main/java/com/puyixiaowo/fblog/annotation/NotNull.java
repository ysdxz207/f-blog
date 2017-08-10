package com.puyixiaowo.fblog.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
public @interface NotNull {
    public String fieldName() default "";

    public String message() default "";
}
