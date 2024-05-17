package com.im.common.entity.tim;

import com.im.common.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 用户每日登陆信息（每人每日只记录一次）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimUserLoginLog extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -6469217865471201433L;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 登陆日期
     */
    private LocalDate loginDate;

    /**
     * 用户客户端类型，可能的取值有"iOS", "Android", "Web", "Windows", "iPad", "Mac", "Linux"
     */
    private String deviceType;

    /**
     * 客户端IP
     */
    private String clientIp;

}
