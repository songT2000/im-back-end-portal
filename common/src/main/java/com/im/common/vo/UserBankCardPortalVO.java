package com.im.common.vo;

import com.im.common.cache.impl.BankCache;
import com.im.common.cache.impl.PortalUserCache;
import com.im.common.entity.UserBankCard;
import com.im.common.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

/**
 * 用户银行卡前台VO
 *
 * @author Barry
 * @date 2021-02-25
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserBankCardPortalVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(UserBankCard.class, UserBankCardPortalVO.class, false);

    public UserBankCardPortalVO(UserBankCard card, BankCache bankCache, PortalUserCache portalUserCache) {
        BEAN_COPIER.copy(card, this, null);

        this.bankName = bankCache.getNameByIdFromLocal(card.getBankId());
        this.cardName = StrUtil.hideWithdrawName(portalUserCache.getUsernameByIdFromLocal(card.getUserId()));
        this.cardNum = StrUtil.hideCardNumber(this.cardNum);
    }

    @ApiModelProperty(value = "ID", position = 1)
    private Long id;

    @ApiModelProperty(value = "银行ID", position = 2)
    private Long bankId;

    @ApiModelProperty(value = "银行名", position = 3)
    private String bankName;

    @ApiModelProperty(value = "卡姓名，已模糊，格式[张**，王**]", position = 4)
    private String cardName;

    @ApiModelProperty(value = "卡号，已模糊，格式[前4位 **** 后4位]", position = 5)
    private String cardNum;

    @ApiModelProperty(value = "支行", position = 6)
    private String branch;

    @ApiModelProperty(value = "是否启用", position = 7)
    private Boolean enabled;
}
