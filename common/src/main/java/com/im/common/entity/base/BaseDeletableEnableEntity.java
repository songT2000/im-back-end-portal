package com.im.common.entity.base;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 所有Entity父类，带有是否启用，已删除字段
 *
 * @author Barry
 * @date 2018/7/28
 */
@Getter
@Setter
public abstract class BaseDeletableEnableEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 5162945968219971063L;

    /**
     * 是否启用
     **/
    @TableField("`is_enabled`")
    protected Boolean enabled;

    /**
     * 是否已删除
     **/
    @TableField("`is_deleted`")
    protected Boolean deleted;
}
