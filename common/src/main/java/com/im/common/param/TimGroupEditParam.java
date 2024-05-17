package com.im.common.param;

import com.im.common.constant.CommonConstant;
import com.im.common.entity.enums.OnOrOffEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 编辑群组参数
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimGroupEditParam {

    /**
     * 群组的ID
     */
    @NotBlank
    @ApiModelProperty(value = "群组ID", required = true)
    private String groupId;

    @NotBlank
    @ApiModelProperty(value = "群名称", required = true, position = 1)
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

    @ApiModelProperty(value = "全员禁言状态", position = 5)
    private OnOrOffEnum shutUpState = OnOrOffEnum.OFF;

    @ApiModelProperty(value = "显示群内成员权限，默认显示", position = 6)
    private Boolean showMemberEnabled = true;

    @ApiModelProperty(value = "上传权限，默认打开", position = 7)
    private Boolean uploadEnabled = true;

    @ApiModelProperty(value = "是否允许普通成员拉人进群，默认关闭", position = 8)
    private Boolean addMemberEnabled = false;

    @ApiModelProperty(value = "群内成员私聊权限，默认关闭", position = 9)
    private Boolean anonymousChatEnabled = false;

    @ApiModelProperty(value = "群内私加好友权限，默认关闭", position = 10)
    private Boolean addFriendEnabled = false;

    @ApiModelProperty(value = "退出群组权限，默认打开", position = 11)
    private Boolean exitEnabled = true;

    @ApiModelProperty(value = "普通成员发消息间隔时间，单位秒,0代表不限制", position = 12)
    private Integer msgIntervalTime = CommonConstant.INT_0;

}
