package com.im.common.vo;

import com.im.common.cache.impl.BankCache;
import com.im.common.entity.BankCardRechargeConfigCard;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

/**
 * 银行卡充值配置
 * @author max.stark
 */
@Data
@NoArgsConstructor
@ApiModel
public class BankCardRechargeConfigCardAdminVO {

    public static final BeanCopier BEAN_COPIER = BeanCopier.create(BankCardRechargeConfigCard.class, BankCardRechargeConfigCardAdminVO.class, false);

    /**
     * @param e  不能为空
     * @param bankCache 不能为空
     */
    public BankCardRechargeConfigCardAdminVO(BankCardRechargeConfigCard e, BankCache bankCache) {
        BEAN_COPIER.copy(e, this, null);
        this.bankName = bankCache.getNameByIdFromLocal(e.getBankId());
    }

    @ApiModelProperty(value = "id", position = 1)
    private Long id;

    @ApiModelProperty(value = "银行ID", position = 2)
    private Long bankId;

    @ApiModelProperty(value = "银行名称", position = 3)
    private String bankName;

    @ApiModelProperty(value = "卡姓名", position = 4)
    private String cardName;

    @ApiModelProperty(value = "卡号", position = 5)
    private String cardNum;

    @ApiModelProperty(value = "支行", position = 6)
    private String cardBranch;

    @ApiModelProperty(value = "是否启用", position = 7)
    private Boolean enabled;

}
