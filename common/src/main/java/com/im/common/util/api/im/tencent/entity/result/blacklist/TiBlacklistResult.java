package com.im.common.util.api.im.tencent.entity.result.blacklist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 拉取黑名单结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TiBlacklistResult {
    /**
     * 用户账号
     */
    private String fromAccount;
    /**
     * 黑名单账号
     */
    private String toAccount;
    /**
     * 加黑名单的时间
     */
    private LocalDateTime addTime;
}
