package com.im.common.cache.sysconfig.vo;

import com.im.common.cache.sysconfig.bo.BaseSysConfigBO;
import com.im.common.cache.sysconfig.bo.RechargeConfigBO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

/**
 * 充值配置
 *
 * @author Barry
 * @date 2021-01-18
 */
@Data
@NoArgsConstructor
@ApiModel
public class RechargeConfigPortalVO extends BaseSysConfigBO {
    private static final BeanCopier BEAN_COPIER = BeanCopier.create(RechargeConfigBO.class, RechargeConfigPortalVO.class, false);

    public RechargeConfigPortalVO(RechargeConfigBO config) {
        BEAN_COPIER.copy(config, this, null);
    }

    @ApiModelProperty(value = "充值是否必须先进行实名认证", position = 1)
    private Boolean authenticationRequired;

    @ApiModelProperty(value = "充值是否必须先绑定资金密码", position = 2)
    private Boolean fundPwdBindRequired;

    @ApiModelProperty(value = "充值是否必须先绑定银行卡", position = 3)
    private Boolean bankCardBindRequired;

    @ApiModelProperty(value = "充值是否必须先绑定虚拟账号", position = 4)
    private Boolean vcAccountBindRequired;
}
