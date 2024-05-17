package com.im.admin.config.permission;

import java.lang.annotation.*;

/**
 * <p>权限验证注解</p>
 *
 * <p>使用该注解在Controller方法上，将检查当前登录用户是否具有指定的权限</p>
 *
 * <p>若检查没有权限时根据，将返回错误给接口</p>
 *
 * <p>具体业务逻辑见{@link com.im.admin.config.AuthInterceptor AuthInterceptor}.</p>
 *
 * <p>
 * 取值优先级如下：
 *     <ul>
 *         <li>url</li>
 *         <li>request.uri</li>
 *     </ul>
 * </p>
 *
 * @author Nick
 * @date 2018/6/12
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckPermission {
    /**
     * 匹配该值与admin_menu.url检查用户是否具有该权限
     * <p>
     * 如果该值不为空，将优先使用该字段
     *
     * @return String
     */
    String[] url() default "";
}
