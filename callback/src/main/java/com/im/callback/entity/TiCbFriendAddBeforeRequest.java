package com.im.callback.entity;

import com.google.gson.annotations.SerializedName;
import com.im.common.entity.enums.AddSourceTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 添加好友之前的回掉
 */
@NoArgsConstructor
@Data
public class TiCbFriendAddBeforeRequest extends TiCallbackRequest{

    private static final long serialVersionUID = -1531592899301531371L;

    /**
     * 请求发起方的 UserID
     */
    @SerializedName("Requester_Account")
    private String requesterAccount;
    /**
     * 请求添加好友的用户的 UserID
     */
    @SerializedName("From_Account")
    private String fromAccount;
    /**
     * 加好友请求的参数
     */
    @SerializedName("FriendItem")
    private List<FriendItemDTO> friendItem;
    /**
     * 加好友方式（默认双向加好友方式）：
     * Add_Type_Single 表示单向加好友
     * Add_Type_Both 表示双向加好友
     */
    @SerializedName("AddType")
    private String addType;
    /**
     * 管理员强制加好友标记：
     * 1 表示强制加好友
     * 0 表示常规加好友方式
     */
    @SerializedName("ForceAddFlags")
    private Integer forceAddFlags;
    /**
     * 毫秒时间戳
     */
    @SerializedName("EventTime")
    private Long eventTime;

    @NoArgsConstructor
    @Data
    public static class FriendItemDTO {
        /**
         * 请求添加的用户的 UserID
         */
        @SerializedName("To_Account")
        private String toAccount;
        /**
         * From_Account 对 To_Account 设置的好友备注
         */
        @SerializedName("Remark")
        private String remark;
        /**
         * From_Account 对 To_Account 设置的好友分组
         */
        @SerializedName("GroupName")
        private String groupName;
        /**
         * 加好友来源
         */
        @SerializedName("AddSource")
        private AddSourceTypeEnum addSource;
        /**
         * 加好友附言
         */
        @SerializedName("AddWording")
        private String addWording;
    }
}
