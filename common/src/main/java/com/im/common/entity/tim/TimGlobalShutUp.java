package com.im.common.entity.tim;

import com.im.common.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimGlobalShutUp extends BaseEntity {
    private static final long serialVersionUID = 3396638255649949166L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 单聊禁言截止时间
     */
    private LocalDateTime c2cShutupEndTime;
    /**
     * 群组禁言截止时间
     */
    private LocalDateTime groupShutupEndTime;
}
