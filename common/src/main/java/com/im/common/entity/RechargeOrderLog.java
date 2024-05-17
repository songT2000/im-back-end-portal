package com.im.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.im.common.entity.base.BaseEntity;
import com.im.common.util.StrUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 充值订单日志
 *
 * @author Barry
 * @date 2020-10-31
 */
@Data
@NoArgsConstructor
public class RechargeOrderLog extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -8845309179793167363L;

    /**
     * 分布式ID
     **/
    @TableId
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 内容
     */
    private String content;

    public RechargeOrderLog(Long orderId, String content, Object... params) {
        this.id = IdWorker.getId();
        this.orderId = orderId;
        this.content = StrUtil.format(content, params);
        this.content = StrUtil.subWithLength(this.content, 0, 2048);
        this.createTime = LocalDateTime.now();
        this.updateTime = this.createTime;
    }
}
