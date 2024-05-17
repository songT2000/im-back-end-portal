package com.im.common.util.aop.log;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.im.common.cache.impl.AdminUserCache;
import com.im.common.cache.impl.PortalUserCache;
import com.im.common.config.RedisConfig;
import com.im.common.constant.CommonConstant;
import com.im.common.entity.enums.DeviceTypeEnum;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.response.RestResponse;
import com.im.common.service.UserOperationLogService;
import com.im.common.util.EnumUtil;
import com.im.common.util.RequestUtil;
import com.im.common.util.ip.IpAddressUtil;
import com.im.common.util.jwt.JwtInfo;
import com.im.common.util.jwt.JwtUtil;
import com.im.common.util.user.UserUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 用户操作日志切面，添加用户操作日志
 *
 * <p>在需要添加用户操作日志的Controller方法上加上{@link UserOperationLog}注解</p>
 *
 * <p>当前用户必须处于登录状态才会正确执行，否则会忽略</p>
 *
 * @author Barry
 * @date 2020-05-23
 */
@Aspect
@Component
@Order(100)
@ConditionalOnBean({RedisConfig.class, PortalUserCache.class, AdminUserCache.class})
public class UserOperationLogAop {
    private UserOperationLogService userOperationLogService;

    @Autowired
    public UserOperationLogAop(UserOperationLogService userOperationLogService) {
        this.userOperationLogService = userOperationLogService;
    }

    /**
     * 切入点
     */
    @Pointcut(value = "@annotation(com.im.common.util.aop.log.UserOperationLog)")
    private void pointCut() {
    }

    /**
     * 通知
     */
    @AfterReturning(pointcut = "pointCut()", returning = "ret")
    private void afterReturning(JoinPoint point, Object ret) throws Throwable {
        if (ret == null) {
            return;
        }
        // 没有request不检查
        HttpServletRequest request = RequestUtil.getRequestFromSpring();
        if (request == null) {
            return;
        }
        // 获取当前登录用户，因为这里不再从redis获取用户，因为之前的拦截器已经执行，没有必要再获取一次，一定是成功的
        String token = JwtUtil.getTokenFromHeader(request);
        if (StrUtil.isBlank(token)) {
            return;
        }
        JwtInfo jwtInfo = JwtUtil.getJwtInfo(token);
        if (jwtInfo == null) {
            return;
        }
        PortalTypeEnum portalType = EnumUtil.valueOfIEnum(PortalTypeEnum.class, jwtInfo.getPortalType());
        Long userId = UserUtil.getUserIdByUsernameFromLocal(jwtInfo.getUsername(), portalType);
        if (userId == null) {
            return;
        }

        Object execResult = ret;
        // 只记录处理成功
        if (execResult instanceof RestResponse && (((RestResponse) execResult)).isOkRsp()) {
            // 获取注解对象
            MethodSignature ms = (MethodSignature) point.getSignature();

            Method method = ms.getMethod();

            UserOperationLog annotation = method.getAnnotation(UserOperationLog.class);

            // 获取RequestBody中请求参数
            String requestParam = getRequestParam(point, method, annotation);

            // 获取注解中用户日志类型
            UserOperationLogTypeEnum operationType = annotation.type();

            // 请求IP
            String ip = RequestUtil.getRequestIp(request);
            String area = IpAddressUtil.findLocation(ip);

            com.im.common.entity.UserOperationLog log = new com.im.common.entity.UserOperationLog();
            log.setUserId(userId);
            log.setPortalType(portalType);
            log.setOperationType(operationType);
            log.setIp(ip);
            log.setArea(area);
            log.setDeviceId(jwtInfo.getDeviceId());
            log.setDeviceType(EnumUtil.valueOfIEnum(DeviceTypeEnum.class, jwtInfo.getDeviceType()));
            log.setRequestParam(requestParam);
            log.setCreateTime(LocalDateTime.now());

            // 存储到redis后续由调度任务统一处理
            userOperationLogService.pushQueen(log);
        }
    }

    /**
     * 获取RequestBody中请求参数
     */
    private String getRequestParam(JoinPoint point, Method method, UserOperationLog paramAnnotation) {
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

        return removeParam(requestParam, paramAnnotation);
    }

    private String removeParam(Object requestBody, UserOperationLog paramAnnotation) {
        if (requestBody == null) {
            return null;
        }

        // 隐藏敏感属性
        if (paramAnnotation.hideParam() != null && paramAnnotation.hideParam().length > 0) {
            JSONObject jsonObject = JSONUtil.parseObj(JSONUtil.toJsonStr(requestBody));

            for (String param : paramAnnotation.hideParam()) {
                if (StrUtil.isNotBlank(param)) {
                    jsonObject.set(param, CommonConstant.ASTERISK);
                }
            }

            return jsonObject.toString();
        }

        return JSON.toJSONString(requestBody);
    }
}
