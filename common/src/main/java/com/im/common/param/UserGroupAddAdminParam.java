package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Set;

/**
 * 用户组新增
 *
 * @author Barry
 * @date 2021-04-12
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserGroupAddAdminParam {
    @NotBlank
    @ApiModelProperty(value = "组名，基本信息tab", required = true, position = 1)
    private String name;

    @ApiModelProperty(value = "备注，可选项，基本信息tab", position = 2)
    private String remark;

    @ApiModelProperty(value = "用户列表tab，可选项", position = 3)
    private Set<String> userList;

    @ApiModelProperty(value = "银行卡充值列表tab，可选项，调用[银行卡充值相关接口-简单列表]", position = 4)
    private Set<Long> bankCardRechargeConfigIdList;

    @ApiModelProperty(value = "三方充值配置列表tab，可选项，调用[三方充值渠道相关接口-简单列表]", position = 5)
    private Set<Long> apiRechargeConfigIdList;
}
