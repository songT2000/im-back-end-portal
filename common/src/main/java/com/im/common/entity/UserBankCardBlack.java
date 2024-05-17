package com.im.common.entity;

import com.im.common.entity.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户卡黑名单
 *
 * @author Barry
 * @date 2020-09-18
 */
@Data
@NoArgsConstructor
public class UserBankCardBlack extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -3406633485733328166L;

    /**
     * 卡姓名
     **/
    private String cardName;

    /**
     * 卡号
     **/
    private String cardNum;

    /**
     * 创建人
     **/
    private Long createAdminId;

    /**
     * 修改人
     **/
    private Long updateAdminId;

    /**
     * 备注
     */
    private String remark;
}
