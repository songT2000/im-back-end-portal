package com.im.callback.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 添加好友后的回掉
 */
@NoArgsConstructor
@Data
public class TiCbFriendAddAfterRequest extends TiCallbackRequest {

    private static final long serialVersionUID = -381922418081277430L;
    /**
     * 成功添加的好友对
     */
    @JSONField(name = "PairList")
    private List<PairListDTO> pairList;
    /**
     * 触发回调的命令字：
     * <li>加好友请求，合理的取值如下：friend_add、FriendAdd</li>
     * <li>加好友回应，合理的取值如下：friend_response、FriendResponse</li>
     */
    @JSONField(name = "ClientCmd")
    private String clientCmd;
    /**
     * 如果当前请求是后台触发的加好友请求，则该字段被赋值为管理员帐号；否则为空
     */
    @JSONField(name = "Admin_Account")
    private String adminAccount;
    /**
     * 管理员强制加好友标记：1 表示强制加好友；0 表示常规加好友方式
     */
    @JSONField(name = "ForceFlag")
    private Integer forceFlag;

    @NoArgsConstructor
    @Data
    public static class PairListDTO {
        /**
         * From_Account 的好友表中增加了 To_Account
         */
        @JSONField(name = "From_Account")
        private String fromAccount;
        /**
         * To_Account 被增加到了 From_Account 的好友表中
         */
        @JSONField(name = "To_Account")
        private String toAccount;
        /**
         * 发起加好友请求的用户的 UserID
         */
        @JSONField(name = "Initiator_Account")
        private String initiatorAccount;
    }
}
