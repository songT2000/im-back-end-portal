package com.im.callback.entity;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 添加好友回应之前的回掉
 */
@NoArgsConstructor
@Data
public class TiCbFriendBeforeResponseRequest extends TiCallbackRequest{
    private static final long serialVersionUID = -4974100062568110485L;

    /**
     * 添加好友回应请求发起方的 UserID
     */
    @SerializedName("Requester_Account")
    private String requesterAccount;
    /**
     * 请求加好友回应的用户的 UserID
     */
    @SerializedName("From_Account")
    private String fromAccount;
    /**
     * 加好友回应请求的参数
     */
    @SerializedName("ResponseFriendItem")
    private List<ResponseFriendItemDTO> responseFriendItem;
    /**
     * 毫秒时间戳
     */
    @SerializedName("EventTime")
    private Long eventTime;

    @NoArgsConstructor
    @Data
    public static class ResponseFriendItemDTO {
        /**
         * 请求回应的用户的 UserID
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
        @SerializedName("TagName")
        private String tagName;
        /**
         * 加好友回应方式：
         * Response_Action_AgreeAndAdd 表示同意且添加对方为好友
         * Response_Action_Agree 表示同意对方加自己为好友
         * Response_Action_Reject 表示拒绝对方的加好友请求
         */
        @SerializedName("ResponseAction")
        private String responseAction;
    }
}
