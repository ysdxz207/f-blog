package com.puyixiaowo.fblog.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author Moses
 * @date 2017-08-27 21:18
 */
@Aspect
public class AdminPermissionsAspect {
    @Around("execution(* *(..)) && @annotation(com.puyixiaowo.fblog.annotation.admin.RequiresPermissions)")
    public Object execute(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        System.out.println(proceedingJoinPoint.getTarget());
        return proceedingJoinPoint.proceed();
    }
}
