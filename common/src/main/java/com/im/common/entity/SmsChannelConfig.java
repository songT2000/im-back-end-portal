package com.im.common.entity;

import com.im.common.entity.base.BaseEntity;
import com.im.common.entity.enums.SmsChannelTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 短信信令通道配置
 *
 * @author Barry
 * @date 2022-02-10
 */
@Data
@NoArgsConstructor
public class SmsChannelConfig extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -2712869835440233479L;

    /**
     * 名称，随意输入
     **/
    private String name;

    /**
     * 编码，随意输入，可以是英文缩写
     **/
    private String code;

    /**
     * 类型，必须要跟系统枚举对应
     **/
    private SmsChannelTypeEnum type;

    /**
     * 三方配置，密钥，URL什么的，JSON格式
     */
    private String thirdConfig;
}
