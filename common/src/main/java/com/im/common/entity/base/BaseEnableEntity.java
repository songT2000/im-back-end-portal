package com.im.common.entity.base;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 所有Entity父类，带有是否启用字段
 *
 * @author Barry
 * @date 2018/7/28
 */
@Getter
@Setter
public abstract class BaseEnableEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 150161184222489615L;

    /**
     * 是否启用true启用，false禁用
     **/
    @TableField("`is_enabled`")
    protected Boolean enabled;
}
