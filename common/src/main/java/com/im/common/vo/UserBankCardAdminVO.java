package com.im.common.vo;

import com.im.common.cache.impl.BankCache;
import com.im.common.cache.impl.PortalUserCache;
import com.im.common.entity.UserBankCard;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDateTime;

/**
 * 用户银行卡后台VO
 *
 * @author Barry
 * @date 2021-02-25
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserBankCardAdminVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(UserBankCard.class, UserBankCardAdminVO.class, false);

    public UserBankCardAdminVO(UserBankCard card, BankCache bankCache, PortalUserCache portalUserCache) {
        BEAN_COPIER.copy(card, this, null);
        this.cardName = portalUserCache.getWithdrawNameByIdFromLocal(card.getUserId());
        this.username = UserUtil.getUsernameByIdFromLocal(card.getUserId(), PortalTypeEnum.PORTAL);
        this.bankName = bankCache.getNameByIdFromLocal(card.getBankId());
    }

    @ApiModelProperty(value = "ID", position = 1)
    private Long id;

    @ApiModelProperty(value = "用户名", position = 2)
    private String username;

    @ApiModelProperty(value = "银行ID", position = 3)
    private Long bankId;

    @ApiModelProperty(value = "银行名", position = 4)
    private String bankName;

    @ApiModelProperty(value = "卡姓名", position = 5)
    private String cardName;

    @ApiModelProperty(value = "卡号", position = 6)
    private String cardNum;

    @ApiModelProperty(value = "支行名称", position = 7)
    private String branch;

    @ApiModelProperty(value = "是否启用", position = 8)
    private Boolean enabled;

    @ApiModelProperty(value = "创建时间", position = 9)
    private LocalDateTime createTime;
}
