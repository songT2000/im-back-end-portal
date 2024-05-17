package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * 用户组编辑相关关联资源ID
 *
 * @author Barry
 * @date 2021-04-12
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserGroupEditRelationIdAdminParam {
    @NotNull
    @ApiModelProperty(value = "组ID", required = true, position = 1)
    private Long groupId;

    @ApiModelProperty(value = "选中列表，注意这是资源的ID，比如[银行卡充值配置ID/三方充值配置ID]", position = 2)
    private Set<Long> ids;
}
