package com.im.common.vo;

import com.im.common.cache.impl.BankCardRechargeConfigCache;
import com.im.common.entity.UserGroupBankCardRechargeConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

/**
 * 组银行卡充值配置后台VO
 *
 * @author Barry
 * @date 2021-04-12
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserGroupBankCardRechargeConfigAdminVO {
    private static final BeanCopier BEAN_COPIER = BeanCopier.create(UserGroupBankCardRechargeConfig.class, UserGroupBankCardRechargeConfigAdminVO.class, false);

    public UserGroupBankCardRechargeConfigAdminVO(UserGroupBankCardRechargeConfig e, BankCardRechargeConfigCache bankCardRechargeConfigCache) {
        BEAN_COPIER.copy(e, this, null);

        this.bankCardRechargeConfigName = bankCardRechargeConfigCache.getNameByIdFromLocal(e.getBankCardRechargeConfigId());
    }

    @ApiModelProperty(value = "ID，删除的时候传这个ID", position = 1)
    private Long id;

    @ApiModelProperty(value = "银行卡充值配置ID", position = 2)
    private Long bankCardRechargeConfigId;

    @ApiModelProperty(value = "银行卡充值配置名", position = 3)
    private String bankCardRechargeConfigName;
}
