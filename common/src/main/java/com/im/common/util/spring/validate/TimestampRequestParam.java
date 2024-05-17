package com.im.common.util.spring.validate;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 对要求时间戳的接口参数继承此类
 * <p>
 * todo 后续外部接口还要加上RSA来保证更安全
 *
 * @author Barry
 * @date 2019/2/21
 */
@Getter
@Setter
public abstract class TimestampRequestParam {
    /**
     * 格式为时间转换为秒值，也就是从1970年1月1日起至今的时间转换为秒（GMT+8为准）。
     * 如该时间戳小于当前时间配置值，不合法
     * 避免重放攻击
     * https://tool.lu/timestamp/
     *
     * @see TimestampValidator 详细实现见这里
     */
    @NotNull(message = "RSP_MSG.TIMESTAMP_REQUIRED#I18N")
    @TimestampValidate(message = "RSP_MSG.TIMESTAMP_EXPIRED#I18N")
    protected Long timestamp;
}