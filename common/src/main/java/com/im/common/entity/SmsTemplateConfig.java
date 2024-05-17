package com.im.common.entity;

import com.im.common.entity.base.BaseEnableEntity;
import com.im.common.util.mybatis.typehandler.i18n.I18nString;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 短信模板配置
 *
 * @author Barry
 * @date 2022-02-10
 */
@Data
@NoArgsConstructor
public class SmsTemplateConfig extends BaseEnableEntity implements Serializable {
    private static final long serialVersionUID = -1733330296908472492L;

    /**
     * 国家名
     */
    private I18nString country;

    /**
     * 国家英文短编码
     **/
    private String code;

    /**
     * 国际区号，+86/+886/+1等
     **/
    private String prefix;

    /**
     * 验证码模板，如果需要【签名】，则要在此处加入
     **/
    private I18nString verificationTemplate;

    /**
     * 信令通道编码，对应sms_channel_config.code
     **/
    private String channelCode;
}
