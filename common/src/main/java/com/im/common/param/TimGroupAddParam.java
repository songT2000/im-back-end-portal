package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * 创建群组参数
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimGroupAddParam {

    /**
     * 群主ID，一般是当前用户ID
     */
    private Long ownerUserId;
    /**
     * 群组的ID
     */
    @ApiModelProperty(value = "群组ID", required = true)
    private String groupId;

    @NotBlank
    @ApiModelProperty(value = "群名称", required = true,position = 1)
    private String groupName;
    /**
     * 群简介
     */
    @ApiModelProperty(value = "群简介", position = 2)
    private String introduction;
    /**
     * 群公告
     */
    @ApiModelProperty(value = "群公告", position = 3)
    private String notification;
    /**
     * 群头像 URL
     */
    @ApiModelProperty(value = "群头像 URL", position = 4)
    private String faceUrl;

    @NotNull
    @Size(min = 1)
    @ApiModelProperty(value = "群内成员ID列表", required = true,position = 5)
    private Set<Long> memberIds;
}
