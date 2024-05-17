package com.im.common.util.aop.log;

import com.im.common.entity.enums.UserOperationLogTypeEnum;

import java.lang.annotation.*;

/**
 * <p>用户操作日志注解</p>
 *
 * <p>使用该注解在Controller方法上，添加用户操作日志</p>
 *
 * <p>当前用户必须处于登录状态才会正确执行，否则会忽略</p>
 *
 * <p>具体业务逻辑见{@link UserOperationLogAop}.</p>
 *
 * @author Barry
 * @date 2020-05-23
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UserOperationLog {
    /**
     * 用户操作日志类型
     */
    UserOperationLogTypeEnum type();

    /**
     * 有一些参数由于比较敏感(比如密码)，不希望在日志中出现，这里就隐藏
     *
     * @return
     */
    String[] hideParam() default "";
}
