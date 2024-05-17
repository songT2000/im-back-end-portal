package com.im.common.entity;

import com.im.common.entity.base.BaseEntity;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 缓存刷新表
 *
 * @author Barry
 * @date 2018/6/8
 */
@Data
@NoArgsConstructor
public class SysCacheRefresh extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 8751289491960534949L;

    /**
     * 缓存类型
     **/
    private SysCacheRefreshTypeEnum type;

    /**
     * 备注
     **/
    private String remark;

    /**
     * 缓存加载完成时间，各个实体类可能会有不同意义，修改实体时，该字段会自动修改为机器当前时间
     **/
    private LocalDateTime finishUpdateTime;

    public SysCacheRefresh(SysCacheRefreshTypeEnum type, String remark) {
        this.type = type;
        this.remark = remark;
        this.finishUpdateTime = LocalDateTime.now();
    }
}
