package com.im.common.util.aop.limit;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.RateLimiter;
import com.im.common.config.RedisConfig;
import com.im.common.constant.RedisKeyEnum;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.util.RequestUtil;
import com.im.common.util.jwt.JwtInfo;
import com.im.common.util.jwt.JwtUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 限速注解实现
 *
 * @author Barry
 * @date 2020-03-21
 */
@Aspect
@Component
@Order(99)
@ConditionalOnBean(RedisConfig.class)
public class RequestLimitAop {
    private static final Log LOG = LogFactory.get();

    /**
     * 根据key分不同的令牌桶, 每3分钟自动清理缓存
     */
    private static Cache<String, RateLimiter> CACHES = CacheBuilder.newBuilder()
            // 清除
            .expireAfterAccess(5, TimeUnit.MINUTES)
            // 最大值,超过这个值会清除掉最近没使用到的缓存
            .maximumSize(1024 * 20)
            .build();

    /**
     * 切入点
     */
    @Pointcut(value = "@annotation(com.im.common.util.aop.limit.RequestLimit)")
    private void pointCut() {
    }

    @Around("pointCut()")
    private Object around(ProceedingJoinPoint point) throws Throwable {
        // 没有request不检查
        HttpServletRequest request = RequestUtil.getRequestFromSpring();
        if (request == null) {
            return point.proceed();
        }

        // 获取注解对象
        MethodSignature ms = (MethodSignature) point.getSignature();

        Method method = ms.getMethod();

        RequestLimit annotation = method.getAnnotation(RequestLimit.class);

        if (annotation.type() == RequestLimitTypeEnum.IP) {
            return limitByIp(point, request, annotation);
        } else if (annotation.type() == RequestLimitTypeEnum.USER) {
            return limitByUser(point, request, annotation);
        } else if (annotation.type() == RequestLimitTypeEnum.GLOBAL) {
            return limitByGlobal(point, request, annotation);
        }  else if (annotation.type() == RequestLimitTypeEnum.REQUEST_BODY_KEY) {
            return limitByRequestBodyKey(point, method, request, annotation);
        } else {
            return point.proceed();
        }
    }

    private Object limitByIp(ProceedingJoinPoint point, HttpServletRequest request, RequestLimit annotation) throws Throwable {
        String url = RequestUtil.getRequestPath(request);
        String ip = RequestUtil.getRequestIp(request);
        if (StrUtil.isBlank(ip) || StrUtil.isBlank(url)) {
            return point.proceed();
        }
        String key = StrUtil.format(RedisKeyEnum.SYS_REQUEST_LIMIT_BY_IP.getVal(), url, ip);

        return limitByKey(point, key, url, annotation);
    }

    private Object limitByUser(ProceedingJoinPoint point, HttpServletRequest request, RequestLimit annotation) throws Throwable {
        if (annotation.second() <= 0 || annotation.count() <= 0) {
            return point.proceed();
        }

        String url = RequestUtil.getRequestPath(request);
        if (StrUtil.isBlank(url)) {
            return point.proceed();
        }
        String token = JwtUtil.getTokenFromHeader(request);
        if (StrUtil.isBlank(token)) {
            return point.proceed();
        }
        JwtInfo jwtInfo = JwtUtil.getJwtInfo(token);
        if (jwtInfo == null) {
            return point.proceed();
        }
        String username = jwtInfo.getUsername();
        if (StrUtil.isBlank(username)) {
            return point.proceed();
        }

        String key = StrUtil.format(RedisKeyEnum.SYS_REQUEST_LIMIT_BY_USER.getVal(), url, username);

        return limitByKey(point, key, url, annotation);
    }

    private Object limitByGlobal(ProceedingJoinPoint point, HttpServletRequest request, RequestLimit annotation) throws Throwable {
        String url = RequestUtil.getRequestPath(request);
        if (StrUtil.isBlank(url)) {
            return point.proceed();
        }

        String key = StrUtil.format(RedisKeyEnum.SYS_REQUEST_LIMIT_BY_GLOBAL.getVal(), url);

        return limitByKey(point, key, url, annotation);
    }

    private Object limitByRequestBodyKey(ProceedingJoinPoint point, Method method, HttpServletRequest request, RequestLimit annotation) throws Throwable {
        if (StrUtil.isBlank(annotation.requestBodyKey())) {
            return point.proceed();
        }

        String url = RequestUtil.getRequestPath(request);
        if (StrUtil.isBlank(url)) {
            return point.proceed();
        }

        String requestKeyValue = getRequestParam(point, method, annotation.requestBodyKey());
        if (StrUtil.isBlank(requestKeyValue)) {
            return point.proceed();
        }

        String key = StrUtil.format(RedisKeyEnum.SYS_REQUEST_LIMIT_BY_REQUEST_BODY_KEY.getVal(), url, requestKeyValue);

        return limitByKey(point, key, url, annotation);
    }

    /**
     * 获取RequestBody中请求参数
     */
    private String getRequestParam(JoinPoint point, Method method, String key) {
        Object requestParam = null;

        // 先拿到所有的参数,根据指定的下标即可拿到对象的值
        Object[] args = point.getArgs();

        // 获取参数的所有注解
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        for (Annotation[] parameterAnnotation : parameterAnnotations) {
            int paramIndex = ArrayUtil.indexOf(parameterAnnotations, parameterAnnotation);
            for (Annotation annotation : parameterAnnotation) {
                if (annotation instanceof RequestBody) {
                    requestParam = args[paramIndex];
                    break;
                }
            }
        }

        if (requestParam == null) {
            return null;
        }

        String requestJson = JSON.toJSONString(requestParam);

        JSONObject jsonObject = JSON.parseObject(requestJson);
        return jsonObject == null ? null : jsonObject.getString(key);
    }

    private Object limitByKey(ProceedingJoinPoint point, String key, String url, RequestLimit annotation) throws Throwable {
        if (annotation.second() <= 0 || annotation.count() <= 0) {
            return point.proceed();
        }

        // 每秒钟允许访问多少次
        double limitPerSecond = annotation.count() * 1.0 / annotation.second();

        RateLimiter rateLimiter;
        try {
            rateLimiter = CACHES.get(key, () -> RateLimiter.create(limitPerSecond));
        } catch (ExecutionException e) {
            LOG.error(e, "限流出错{}", url);
            return point.proceed();
        }

        // 获取许可
        boolean acquire = rateLimiter.tryAcquire(1, TimeUnit.SECONDS);
        if (!acquire) {
            return RestResponse.failed(ResponseCode.SYS_REQUEST_LIMITED, url);
        }

        return point.proceed();
    }
}