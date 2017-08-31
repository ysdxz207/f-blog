package com.puyixiaowo.fblog.aop;

import com.puyixiaowo.fblog.annotation.admin.Logical;
import com.puyixiaowo.fblog.annotation.admin.RequiresPermissions;
import com.puyixiaowo.fblog.exception.NoPermissionsException;
import com.puyixiaowo.fblog.service.UserService;
import com.puyixiaowo.fblog.utils.Assert;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import spark.Request;

/**
 * @author Moses
 * @date 2017-08-27 21:18
 */
@Aspect
public class AdminPermissionsAspect {

    @Pointcut("execution(public * *(..))")
    void annotatedMethod() {}


    @Before("annotatedMethod() && @annotation(requiresPermissions)")
    public void execute(JoinPoint joinPoint, RequiresPermissions requiresPermissions) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//        RequiresPermissions requiresPermissions = method.getAnnotation(RequiresPermissions.class);

        if (requiresPermissions == null) {
            return;
        }
        Object[] args = joinPoint.getArgs();

        Request request = null;
        for (Object object : args) {
            if (object instanceof Request) {
                request = (Request) object;
                break;
            }
        }

        Assert.notNull(request, "RequiresPermissions注解的方法需要参数Spark.Request。");


        String[] permissions = requiresPermissions.value();
        Logical logical = requiresPermissions.logical();

        if (permissions.length == 0) {
            throw new NoPermissionsException("没有访问权限!");
        }

        if (!UserService.currentUserHasPermissions(request, permissions, logical)) {
            throw new NoPermissionsException("没有访问权限!");
        }

    }
}
