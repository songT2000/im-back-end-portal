package com.im.common.util.api.im.tencent.entity.param.friend;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.entity.enums.AddSourceTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 添加好友参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TiFriendAddParam {

    /**
     * 需要为该 UserID 添加好友(必填)
     */
    @JSONField(name = "From_Account")
    private String fromAccount;
    /**
     * 好友结构体
     */
    @JSONField(name = "AddFriendItem")
    private List<AddFriendItemDTO> addFriendItem;
    /**
     * 管理员强制加好友标记：1表示强制加好友，0表示常规加好友方式
     */
    @JSONField(name = "ForceAddFlags")
    private Integer forceAddFlags = 1;

    public TiFriendAddParam(String fromAccount, List<AddFriendItemDTO> addFriendItem) {
        this.fromAccount = fromAccount;
        this.addFriendItem = addFriendItem;
    }

    /**
     * 好友结构体对象
     */
    @NoArgsConstructor
    @Data
    public static class AddFriendItemDTO {
        /**
         * 好友的 UserID（必填）
         */
        @JSONField(name = "To_Account")
        private String toAccount;
        /**
         * 加好友来源(必填)：
         * <br>1. 加好友来源字段包含前缀和关键字两部分；
         * <br>2. 加好友来源字段的前缀是：AddSource_Type_ ；
         * <br>3. 关键字：必须是英文字母，且长度不得超过 8 字节，建议用一个英文单词或该英文单词的缩写；
         * <br>4. 示例：加好友来源的关键字是 Android，则加好友来源字段是：AddSource_Type_Android
         */
        @JSONField(name = "AddSource")
        private AddSourceTypeEnum addSource;
        /**
         * From_Account 对 To_Account 的好友备注(选填)
         * <br>备注长度最长不得超过 96 个字节
         */
        @JSONField(name = "Remark")
        private String remark;
        /**
         * From_Account 对 To_Account 的分组信息
         */
        @JSONField(name = "GroupName")
        private String groupName;
        /**
         * From_Account 和 To_Account 形成好友关系时的附言信息
         * <br>加好友附言的长度最长不得超过 256 个字节
         */
        @JSONField(name = "AddWording")
        private String addWording;

        public AddFriendItemDTO(String toAccount, AddSourceTypeEnum addSource) {
            this.toAccount = toAccount;
            this.addSource = addSource;
        }
    }
}
