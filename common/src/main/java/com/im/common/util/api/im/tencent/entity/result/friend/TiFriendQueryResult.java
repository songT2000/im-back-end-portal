package com.im.common.util.api.im.tencent.entity.result.friend;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.im.common.util.api.im.tencent.entity.TiCustomItem;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 拉取好友结果
 */
@Data
@NoArgsConstructor
public class TiFriendQueryResult extends TiBaseResult {

    /**
     * 好友对象数组，每一个好友对象都包含一个 To_Account 字段和一个 ValueItem 数组
     */
    @JSONField(name = "UserDataItem")
    private List<UserDataItem> userDataItems;
    /**
     * 标配好友数据的 Sequence，客户端可以保存该 Sequence，下次请求时通过请求的 StandardSequence 字段返回给后台
     */
    @JSONField(name = "StandardSequence")
    private Integer standardSequence;
    /**
     * 自定义好友数据的 Sequence，客户端可以保存该 Sequence，下次请求时通过请求的 CustomSequence 字段返回给后台
     */
    @JSONField(name = "CustomSequence")
    private Integer customSequence;
    /**
     * 好友总数
     */
    @JSONField(name = "FriendNum")
    private Integer friendNum;
    /**
     * 分页的结束标识，非0值表示已完成全量拉取
     */
    @JSONField(name = "CompleteFlag")
    private Integer completeFlag;
    /**
     * 分页接口下一页的起始位置
     */
    @JSONField(name = "NextStartIndex")
    private Integer nextStartIndex;


    /**
     * 批量加好友的结果对象
     */
    @Data
    @NoArgsConstructor
    public static class UserDataItem {
        /**
         * 好友的 UserID
         */
        @JSONField(name = "To_Account")
        private String toAccount;
        /**
         * 保存好友数据的数组，数组每一个元素都包含一个 Tag 字段和一个 Value 字段
         */
        @JSONField(name = "ValueItem")
        private List<TiCustomItem> values;
    }
}
