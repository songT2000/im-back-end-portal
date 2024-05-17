package com.im.common.vo;

import com.im.common.entity.PortalUser;
import com.im.common.entity.RechargeOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 人工充值获取用户信息时返回的信息
 *
 * @author Barry
 * @date 2021-03-24
 */
@Data
@NoArgsConstructor
@ApiModel
public class PortalUserRechargeAdminVO {
    private static final BeanCopier BEAN_COPIER = BeanCopier.create(PortalUser.class, PortalUserRechargeAdminVO.class, false);

    public PortalUserRechargeAdminVO(PortalUser user, RechargeOrder lastRecharge) {
        BEAN_COPIER.copy(user, this, null);
        this.lastRechargeTime = lastRecharge == null ? null : lastRecharge.getCreateTime();
        this.lastRechargeAmount = lastRecharge == null ? null : lastRecharge.getPayAmount();
    }

    @ApiModelProperty(value = "用户名", position = 1)
    private String username;

    @ApiModelProperty(value = "余额", position = 2)
    private BigDecimal balance;

    @ApiModelProperty(value = "上次充值时间", position = 3)
    private LocalDateTime lastRechargeTime;

    @ApiModelProperty(value = "上次充值金额", position = 4)
    private BigDecimal lastRechargeAmount;
}
