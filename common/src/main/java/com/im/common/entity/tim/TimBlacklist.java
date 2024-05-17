package com.im.common.entity.tim;

import com.im.common.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户黑名单
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimBlacklist extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -5322396727872529031L;
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 黑名单用户ID
     */
    private Long blacklistUserId;

}
