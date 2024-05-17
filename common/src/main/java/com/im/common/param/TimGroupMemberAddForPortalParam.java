package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

/**
 * app添加群组成员
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimGroupMemberAddForPortalParam {
    @NotBlank
    @ApiModelProperty(value = "群组ID", required = true, position = 1)
    private String groupId;

    @NotNull
    @Size(min = 1)
    @ApiModelProperty(value = "添加用户的账号集合", required = true, position = 2)
    private Set<String> usernameList;

}
