package com.smart.framework.crud.datapermission.aspect;

import com.smart.framework.crud.datapermission.annotation.SmartDataPermission;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 注解SmartDataPermission切面
 * 数据权限信息注入上下文
 * @author shizhongming
 * 2025/3/20 12:51
 * @since 5.0.0
 */
@Aspect
public class DataPermissionContextAspect {

    @Around("@annotation(com.smart.framework.crud.datapermission.annotation.SmartDataPermission)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Signature signature = point.getSignature();
        if (!(signature instanceof MethodSignature methodSignature)) {
            return point.proceed();
        }
        Method method = methodSignature.getMethod();
        List<SmartDataPermission> list = MergedAnnotations.from(method).stream(SmartDataPermission.class)
                .map(MergedAnnotation::synthesize)
                .toList();
        if (CollectionUtils.isEmpty(list)) {
            return point.proceed();
        }
        // 注入上下文
        try {
            DataPermissionContextHolder.set(list);
            return point.proceed();
        } finally {
            // 确保上下文影响范围在该函数内
            DataPermissionContextHolder.clear();
        }
    }
}
