package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 删除群组成员
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimGroupMemberDeleteParam {
    @NotBlank
    @ApiModelProperty(value = "群组ID", required = true, position = 1)
    private String groupId;

    @NotNull
    @Size(min = 1)
    @ApiModelProperty(value = "删除用户账号集合", required = true, position = 2)
    private List<String> usernameList;

    @ApiModelProperty(value = "删除原因", position = 3)
    private String reason;

    @ApiModelProperty(value = "是否清空该用户的聊天记录，默认不清空", position = 4)
    private Boolean removeMsgRecord = false;
}
