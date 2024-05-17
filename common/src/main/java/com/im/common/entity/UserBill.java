package com.im.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.im.common.entity.base.BaseEntity;
import com.im.common.entity.enums.UserBillTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户账变
 *
 * @author Barry
 * @date 2021-08-21
 */
@Data
@NoArgsConstructor
public class UserBill extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -8565422612338838944L;

    /**
     * 分布式ID
     **/
    @TableId
    private Long id;

    /**
     * 用户ID
     **/
    private Long userId;

    /**
     * 账变类型
     */
    private UserBillTypeEnum type;

    /**
     * 账变金额
     */
    private BigDecimal amount;

    /**
     * 金额
     */
    private BigDecimal balance;

    /**
     * 订单号，对应业务的订单号
     */
    private String orderNum;

    /**
     * 备注
     */
    private String remark;

    /**
     * 记账日
     */
    private String reportDate;
}
