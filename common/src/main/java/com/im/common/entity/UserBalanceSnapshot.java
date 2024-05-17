package com.im.common.entity;

import com.im.common.entity.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户余额快照
 * @author max.stark
 */
@Data
@NoArgsConstructor
public class UserBalanceSnapshot extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 4438911101464388647L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 日期
     */
    private String date;

    /**
     * 余额
     */
    private BigDecimal balance;

}
