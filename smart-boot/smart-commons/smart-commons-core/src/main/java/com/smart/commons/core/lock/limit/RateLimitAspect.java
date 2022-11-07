package com.smart.commons.core.lock.limit;

import com.smart.commons.core.exception.RateLimitException;
import com.smart.commons.core.exception.SystemException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * 限流切换
 * @author ShiZhongMing
 * 2022/8/25
 * @since 3.0.0
 */
@Aspect
@Slf4j
public class RateLimitAspect {

    @Setter
    private RateLimitService rateLimitService;

    @Pointcut("@annotation(com.smart.commons.core.lock.limit.RateLimit)")
    public void limiterPointCut() {
        // do nothing
    }

    @Around("limiterPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Signature signature = point.getSignature();

        if (signature instanceof MethodSignature) {
            RateLimit rateLimit = AnnotationUtils.getAnnotation(((MethodSignature)signature).getMethod(), RateLimit.class);
            if (rateLimit == null) {
                throw new SystemException("系统发生未知错误");
            }
            boolean result = this.rateLimitService.acquire(rateLimit.value(), rateLimit.limit());
            if (!result) {
                log.warn("超出最大访问速度，触发限流，限流key：{}，每秒最大访问次数：{}", rateLimit.value(), rateLimit.limit());
                throw new RateLimitException(rateLimit.message());
            }
        }
        return point.proceed();
    }
}
