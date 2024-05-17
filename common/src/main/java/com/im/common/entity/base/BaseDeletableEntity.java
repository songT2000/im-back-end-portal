package com.im.common.entity.base;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 所有Entity父类，带有已删除字段
 *
 * @author Barry
 * @date 2018/7/28
 */
@Getter
@Setter
public abstract class BaseDeletableEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 3777069661439724961L;

    /**
     * 是否已删除
     **/
    @TableField("`is_deleted`")
    protected Boolean deleted;
}
