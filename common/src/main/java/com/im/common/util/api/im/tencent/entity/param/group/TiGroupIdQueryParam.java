package com.im.common.util.api.im.tencent.entity.param.group;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.LongCodec;
import com.google.gson.annotations.SerializedName;
import com.im.common.entity.enums.GroupTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *  获取 App 中的所有群组
 */
@NoArgsConstructor
@Data
public class TiGroupIdQueryParam implements Serializable {

    public static final int MAX_LIMIT = 10000;

    /**
     * 本次获取的群组 ID 数量的上限，不得超过 10000。如果不填，默认为最大值 10000
     */
    @JSONField(name = "Limit")
    private Integer limit;
    /**
     * 群太多时分页拉取标志，第一次填0，以后填上一次返回的值，返回的 Next 为0代表拉完了
     */
    @JSONField(name = "Next",serializeUsing = LongCodec.class)
    private Long next;
    /**
     * <pre>
     * 如果仅需要返回特定群组形态的群组，可以通过 GroupType 进行过滤，
     * 但此时返回的 TotalCount 的含义就变成了 App 中属于该群组形态的群组总数。
     * 不填为获取所有类型的群组。
     * 群组形态包括 Public（公开群），Private（即 Work，好友工作群）
     * ，ChatRoom（即 Meeting，会议群），AVChatRoom（音视频聊天室）和BChatRoom（在线成员广播大群）
     * </pre>
     */
    @JSONField(name = "GroupType")
    private GroupTypeEnum groupType;
}
