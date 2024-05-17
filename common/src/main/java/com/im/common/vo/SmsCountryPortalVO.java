package com.im.common.vo;

import com.im.common.entity.SmsTemplateConfig;
import com.im.common.util.mybatis.typehandler.i18n.I18nString;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

/**
 * @author Barry
 * @date 2021-03-24
 */
@Data
@NoArgsConstructor
@ApiModel
public class SmsCountryPortalVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(SmsTemplateConfig.class, SmsCountryPortalVO.class, false);

    public SmsCountryPortalVO(SmsTemplateConfig e) {
        BEAN_COPIER.copy(e, this, null);
    }

    @ApiModelProperty(value = "国家名", position = 1)
    private I18nString country;

    @ApiModelProperty(value = "国家编码", position = 2)
    private String code;

    @ApiModelProperty(value = "国际区号，+86/+886/+1等", position = 3)
    private String prefix;
}
