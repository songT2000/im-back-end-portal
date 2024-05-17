package com.im.common.cache.sysconfig.vo;

import com.im.common.cache.sysconfig.bo.BaseSysConfigBO;
import com.im.common.cache.sysconfig.bo.WithdrawConfigBO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

/**
 * 提现配置
 *
 * @author Barry
 * @date 2021-01-18
 */
@Data
@NoArgsConstructor
@ApiModel
public class WithdrawConfigPortalVO extends BaseSysConfigBO {
    private static final BeanCopier BEAN_COPIER = BeanCopier.create(WithdrawConfigBO.class, WithdrawConfigPortalVO.class, false);

    public WithdrawConfigPortalVO(WithdrawConfigBO config) {
        BEAN_COPIER.copy(config, this, null);
    }

    @ApiModelProperty(value = "启用时间段，显示就好了", position = 1)
    private String enableTime;

    @ApiModelProperty(value = "最多绑定银行卡数，包含启用和禁用的，小于等于0表示不限制", position = 2)
    private Integer maxBindBankCardCount;

    @ApiModelProperty(value = "最多绑定虚拟账户数，包含启用和禁用的，小于等于0表示不限制", position = 3)
    private Integer maxBindVcAccountCount;
}
