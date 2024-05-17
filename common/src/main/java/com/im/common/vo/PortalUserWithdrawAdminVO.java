package com.im.common.vo;

import com.im.common.entity.PortalUser;
import com.im.common.entity.WithdrawOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 人工提现获取用户信息时返回的信息
 *
 * @author Barry
 * @date 2021-03-24
 */
@Data
@NoArgsConstructor
@ApiModel
public class PortalUserWithdrawAdminVO {
    private static final BeanCopier BEAN_COPIER = BeanCopier.create(PortalUser.class, PortalUserWithdrawAdminVO.class, false);

    public PortalUserWithdrawAdminVO(PortalUser user, WithdrawOrder lastWithdraw) {
        BEAN_COPIER.copy(user, this, null);
        this.lastWithdrawTime = lastWithdraw == null ? null : lastWithdraw.getCreateTime();
        this.lastWithdrawAmount = lastWithdraw == null ? null : lastWithdraw.getRequestAmount();
    }

    @ApiModelProperty(value = "用户名", position = 1)
    private String username;

    @ApiModelProperty(value = "余额", position = 2)
    private BigDecimal balance;

    @ApiModelProperty(value = "上次提现时间", position = 3)
    private LocalDateTime lastWithdrawTime;

    @ApiModelProperty(value = "上次提现金额", position = 4)
    private BigDecimal lastWithdrawAmount;
}
