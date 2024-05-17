package com.im.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.im.common.entity.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 银行
 *
 * @author Barry
 * @date 2019-11-06
 */
@Data
@NoArgsConstructor
public class Bank extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 3184079687319908055L;

    /**
     * 编码
     **/
    private String code;

    /**
     * 银行名
     **/
    private String name;

    /**
     * 排序号
     **/
    private Integer sort;

    /**
     * 是否启用提现
     */
    @TableField("`is_withdraw_enabled`")
    private Boolean withdrawEnabled;
}
