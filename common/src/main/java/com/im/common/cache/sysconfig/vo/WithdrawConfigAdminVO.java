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
public class WithdrawConfigAdminVO extends BaseSysConfigBO {
    private static final BeanCopier BEAN_COPIER = BeanCopier.create(WithdrawConfigBO.class, WithdrawConfigAdminVO.class, false);

    public WithdrawConfigAdminVO(WithdrawConfigBO config) {
        BEAN_COPIER.copy(config, this, null);
    }

    @ApiModelProperty(value = "是否需要审核步骤，用户提交提现订单后，是否需要审核步骤", position = 1)
    private Boolean needApprove;
}
