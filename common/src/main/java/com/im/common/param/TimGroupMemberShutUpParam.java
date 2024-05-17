package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 群组成员禁言
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimGroupMemberShutUpParam {
    @NotBlank
    @ApiModelProperty(value = "群组ID", required = true, position = 1)
    private String groupId;

    @NotNull
    @ApiModelProperty(value = "禁言用户ID集合", required = true, position = 2)
    private List<Long> userIds;

    @NotNull
    @ApiModelProperty(value = "需禁言时间，单位为秒，为0时表示取消禁言，4294967295为永久禁言", required = true, position = 3)
    private Long shutUpTime;
}
