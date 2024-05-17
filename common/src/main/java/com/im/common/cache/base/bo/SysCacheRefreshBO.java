package com.im.common.cache.base.bo;

import com.alibaba.fastjson.JSON;
import com.im.common.entity.SysCacheRefresh;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDateTime;

/**
 * 系统缓存刷新 缓存对象
 *
 * @author Daniel
 * @date 2019/10/18
 */
@Data
@NoArgsConstructor
public class SysCacheRefreshBO {
    private static final BeanCopier BEAN_COPIER = BeanCopier.create(SysCacheRefresh.class, SysCacheRefreshBO.class, false);

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新完成时间
     */
    private LocalDateTime finishUpdateTime;

    public SysCacheRefreshBO(LocalDateTime updateTime, LocalDateTime finishUpdateTime) {
        this.updateTime = updateTime;
        this.finishUpdateTime = finishUpdateTime;
    }

    public SysCacheRefreshBO(SysCacheRefresh sysCacheRefresh) {
        BEAN_COPIER.copy(sysCacheRefresh, this, null);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
