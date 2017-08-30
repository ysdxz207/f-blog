package com.puyixiaowo.fblog.aop;

import com.puyixiaowo.fblog.annotation.admin.Logical;
import com.puyixiaowo.fblog.annotation.admin.RequiresPermissions;
import com.puyixiaowo.fblog.exception.NoPermissionsException;
import com.puyixiaowo.fblog.service.UserService;
import com.puyixiaowo.fblog.utils.Assert;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import spark.Request;

import java.lang.reflect.Method;

/**
 * @author Moses
 * @date 2017-08-27 21:18
 */
@Aspect
public class AdminPermissionsAspect {
    @Around("execution(* *(..)) && @annotation(com.puyixiaowo.fblog.annotation.admin.RequiresPermissions)")
    public Object execute(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        Object[] args = proceedingJoinPoint.getArgs();

        Request request = null;
        for (Object object : args) {
            if (object.getClass().equals(Request.class)) {
                request = (Request) object;
                break;
            }
        }

        Assert.notNull(request, "RequiresPermissions注解的方法需要参数Spark.Request。");

        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();

        RequiresPermissions requiresPermissions = method.getAnnotation(RequiresPermissions.class);
        String[] permissions = requiresPermissions.value();
        Logical logical = requiresPermissions.logical();

        if (permissions.length > 0
                && logical.equals(Logical.AND)
                && !UserService.currentUserHasPermissions(request, permissions)) {
            throw new NoPermissionsException("没有访问权限!");
        }


        return proceedingJoinPoint.proceed();
    }
}
