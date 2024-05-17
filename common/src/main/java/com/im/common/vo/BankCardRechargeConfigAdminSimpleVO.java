package com.im.common.vo;

import com.im.common.entity.BankCardRechargeConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

/**
 * 银行卡充值配置后台简单VO
 */
@Data
@NoArgsConstructor
@ApiModel
public class BankCardRechargeConfigAdminSimpleVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(BankCardRechargeConfig.class, BankCardRechargeConfigAdminSimpleVO.class, false);

    public BankCardRechargeConfigAdminSimpleVO(BankCardRechargeConfig e) {
        BEAN_COPIER.copy(e, this, null);
    }

    @ApiModelProperty(value = "id", position = 1)
    private Long id;

    @ApiModelProperty(value = "名称", position = 2)
    private String name;

    @ApiModelProperty(value = "是否启用", position = 3)
    private Boolean enabled;
}
