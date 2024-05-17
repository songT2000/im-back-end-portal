package com.im.common.vo;

import com.im.common.response.ResponseCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 群组成员进群出群返回信息
 */
@Data
@NoArgsConstructor
@ApiModel
@AllArgsConstructor
public class TimGroupMemberAddVO {

    public TimGroupMemberAddVO(Long userId, String username, Boolean success) {
        this.userId = userId;
        this.username = username;
        this.success = success;
    }

    @ApiModelProperty("成员ID")
    private Long userId;

    @ApiModelProperty("成员账号")
    private String username;

    @ApiModelProperty("是否成功")
    private Boolean success;

    @ApiModelProperty("失败原因")
    private ResponseCode responseCode;
}
