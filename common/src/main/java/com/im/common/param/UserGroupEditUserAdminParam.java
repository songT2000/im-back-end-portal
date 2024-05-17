package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * 用户组编辑用户
 *
 * @author Barry
 * @date 2021-04-12
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserGroupEditUserAdminParam {
    @NotNull
    @ApiModelProperty(value = "组ID", required = true, position = 1)
    private Long groupId;

    @ApiModelProperty(value = "新增用户名列表", position = 2)
    private Set<String> addList;

    @ApiModelProperty(value = "删除用户名列表", position = 3)
    private Set<String> deleteList;
}
