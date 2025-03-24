package com.smart.framework.commons.core.proxy;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 子类继承动态代理
 * @author shizhongming
 * 2024/3/11 17:14
 * @since 3.0.0
 */
public class ExtendMethodInterceptor<T> implements MethodInterceptor {

    private final T target;

    private final List<String> methodNameList;

    public ExtendMethodInterceptor(T target) {
        this(target, null);
    }

    public ExtendMethodInterceptor(T target, List<String> methodNameList) {
        this.target = target;
        this.methodNameList = methodNameList;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if (CollectionUtils.isEmpty(methodNameList) || methodNameList.contains(method.getName())) {
            return method.invoke(this.target, args);
        }
        return proxy.invokeSuper(obj, args);
    }
}
