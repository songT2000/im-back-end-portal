package com.im.common.util.api.im.tencent.entity.result.friend;

import com.im.common.entity.enums.AddSourceTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 拉取好友结果
 */
@Data
@NoArgsConstructor
public class TiFriendResult {
    /**
     * 好友账号
     */
    private String friendAccount;
    /**
     * 好友分组，多个使用逗号分割
     */
    private String groups;
    /**
     * 好友备注名
     */
    private String remark;
    /**
     * 加好友来源
     */
    private AddSourceTypeEnum addSource;
    /**
     * 加好友附言
     */
    private String addWording;
    /**
     * 加好友的时间
     */
    private LocalDateTime addTime;
}
