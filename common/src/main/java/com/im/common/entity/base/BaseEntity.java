package com.im.common.entity.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 所有Entity父类，所有表及实时类都必须要有以下三个字段，任何情况下都必须要有这三个字段
 *
 * <ul>
 *     <li>id</li>
 *     <li>create_time</li>
 *     <li>update_time</li>
 * </ul>
 *
 * @author Barry
 * @date 2018/7/28
 */
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = 8012530283456452834L;

    /**
     * ID，默认策略是自增长ID，如果需要分布式ID，把type删掉就可以了
     **/
    @TableId(type = IdType.AUTO)
    protected Long id;

    /**
     * 创建时间，各个实体类可能会有不同意义，新增实体时，该字段会自动填充为机器当前时间
     **/
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime createTime;

    /**
     * 修改时间，各个实体类可能会有不同意义，修改实体时，该字段会自动修改为机器当前时间
     **/
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected LocalDateTime updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
