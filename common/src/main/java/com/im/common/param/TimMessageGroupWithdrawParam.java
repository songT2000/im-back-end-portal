package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 群组消息撤回请求
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimMessageGroupWithdrawParam {
    @NotBlank
    @ApiModelProperty(value = "群组ID", required = true, position = 1)
    private String groupId;

    @NotNull
    @ApiModelProperty(value = "撤回的消息序列号", required = true, position = 2)
    private List<Long> msgSeqList;

}
