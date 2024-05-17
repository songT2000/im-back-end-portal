package com.im.common.vo;

import com.im.common.entity.BankCardWithdrawConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

/**
 * 银行卡提现配置后台简单VO
 */
@Data
@NoArgsConstructor
@ApiModel
public class BankCardWithdrawConfigAdminSimpleVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(BankCardWithdrawConfig.class, BankCardWithdrawConfigAdminSimpleVO.class, false);

    public BankCardWithdrawConfigAdminSimpleVO(BankCardWithdrawConfig e) {
        BEAN_COPIER.copy(e, this, null);
    }

    @ApiModelProperty(value = "id", position = 1)
    private Long id;

    @ApiModelProperty(value = "名称", position = 2)
    private String name;

    @ApiModelProperty(value = "是否启用", position = 3)
    private Boolean enabled;
}
