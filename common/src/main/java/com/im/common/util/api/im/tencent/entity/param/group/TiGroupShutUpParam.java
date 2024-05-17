package com.im.common.util.api.im.tencent.entity.param.group;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.LongCodec;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 群组禁言
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TiGroupShutUpParam implements Serializable {

    public static final long forever = 4294967295L;

    /**
     * 群组 ID
     */
    @JSONField(name = "GroupId")
    private String groupId;
    /**
     * 需要禁言的用户帐号，最多支持500个帐号
     */
    @JSONField(name = "Members_Account")
    private List<String> accounts;
    /**
     * 需禁言时间，单位为秒，为0时表示取消禁言，4294967295为永久禁言。
     */
    @JSONField(name = "ShutUpTime",serializeUsing = LongCodec.class)
    private Long shutUpTime;
}
