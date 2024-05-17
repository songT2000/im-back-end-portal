package com.im.common.util.api.pay.base.recharge;

import com.im.common.entity.enums.RechargeConfigGroupEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 充值配置分组前台VO
 *
 * @author Barry
 * @date 2021-03-21
 */
@Data
@NoArgsConstructor
@ApiModel
public class RechargeConfigGroupPortalVO {
    @ApiModelProperty(value = "分组", position = 1)
    private RechargeConfigGroupEnum group;

    @ApiModelProperty(value = "配置列表", position = 2)
    private List<RechargeConfigGroupConfigPortalVO> list;
}
