package com.puyixiaowo.fblog.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Moses
 * @date 2017-08-27 15:29
 */
public class AdminPermissionsInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println(invocation.getMethod().getName());
        return null;
    }
}
