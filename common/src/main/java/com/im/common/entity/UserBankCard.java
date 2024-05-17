package com.im.common.entity;

import com.im.common.entity.base.BaseEnableEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户银行卡
 *
 * @author Barry
 * @date 2020-09-18
 */
@Data
@NoArgsConstructor
public class UserBankCard extends BaseEnableEntity implements Serializable {
    private static final long serialVersionUID = 816454464039806051L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 银行ID
     */
    private Long bankId;

    /**
     * 卡号
     **/
    private String cardNum;

    /**
     * 支行名称
     */
    private String branch;
}
