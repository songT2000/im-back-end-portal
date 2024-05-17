package com.im.common.util.api.pay.base.withdraw;

import com.im.common.entity.enums.WithdrawConfigGroupEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 提现配置分组前台VO
 *
 * @author Barry
 * @date 2021-03-21
 */
@Data
@NoArgsConstructor
@ApiModel
public class WithdrawConfigGroupPortalVO {
    @ApiModelProperty(value = "分组", position = 1)
    private WithdrawConfigGroupEnum group;

    @ApiModelProperty(value = "配置列表", position = 2)
    private List<WithdrawConfigGroupConfigPortalVO> list;
}
