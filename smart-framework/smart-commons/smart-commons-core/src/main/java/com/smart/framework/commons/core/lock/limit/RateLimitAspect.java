package com.smart.framework.commons.core.lock.limit;

import com.smart.framework.commons.core.exception.RateLimitException;
import com.smart.framework.commons.core.exception.SystemException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 限流切换
 * @author ShiZhongMing
 * 2022/8/25
 * @since 3.0.0
 */
@Aspect
@Slf4j
public class RateLimitAspect {

    private static final String DEFAULT_KEY_FORMAT = "%s#%s(%s)";

    @Setter
    private RateLimitService rateLimitService;

    @Pointcut("@annotation(com.smart.framework.commons.core.lock.limit.RateLimit)")
    public void limiterPointCut() {
        // do nothing
    }

    @Around("limiterPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        var signature = point.getSignature();

        if (signature instanceof MethodSignature methodSignature) {
            var rateLimit = AnnotationUtils.getAnnotation(methodSignature.getMethod(), RateLimit.class);
            if (rateLimit == null) {
                throw new SystemException("系统发生未知错误");
            }
            String key = rateLimit.value();
            if (!StringUtils.hasText(key)) {
                key = getDefaultKey(methodSignature);
            }
            var result = this.rateLimitService.acquire(key, rateLimit.limit(), rateLimit.unit());
            if (!result) {
                log.warn("超出最大访问速度，触发限流，限流key：{}，每秒最大访问次数：{}", key, rateLimit.limit());
                throw new RateLimitException(rateLimit.message());
            }
        }
        return point.proceed();
    }

    /**
     * 获取默认的key
     * @param methodSignature 方法签名
     * @return key
     */
    private String getDefaultKey(MethodSignature methodSignature) {
        Method method = methodSignature.getMethod();

        String className = method.getDeclaringClass().getName();
        String methodName = method.getName();
        // 参数类型列表
        Class<?>[] parameterTypes = method.getParameterTypes();
        String params = Arrays.stream(parameterTypes)
                .map(Class::getName)
                .collect(Collectors.joining(", "));

        return String.format(DEFAULT_KEY_FORMAT, className, methodName, params);
    }
}
