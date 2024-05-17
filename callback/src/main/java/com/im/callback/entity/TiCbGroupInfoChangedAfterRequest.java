package com.im.callback.entity;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 群组资料修改之后回调
 */
@NoArgsConstructor
@Data
public class TiCbGroupInfoChangedAfterRequest extends TiCallbackRequest{

    private static final long serialVersionUID = 7390523676500788059L;

    /**
     * 群资料被修改的群组 ID
     */
    @SerializedName("GroupId")
    private String groupId;
    /**
     * 群资料被修改的群的 群组类型介绍，例如 Public
     */
    @SerializedName("Type")
    private String type;
    /**
     * 操作者 UserID
     */
    @SerializedName("Operator_Account")
    private String operatorAccount;
    /**
     * 修改后的群名称
     */
    @SerializedName("Name")
    private String name;
    /**
     * 修改后的群简介
     */
    @SerializedName("Introduction")
    private String introduction;
    /**
     * 修改后的群公告
     */
    @SerializedName("Notification")
    private String notification;

    /**
     * 修改后的群头像 URL
     */
    @SerializedName("FaceUrl")
    private String faceUrl;

}
