package com.im.common.vo;

import com.im.common.cache.base.bo.SysCacheRefreshBO;
import com.im.common.entity.SysCacheRefresh;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 系统缓存刷新 缓存对象
 *
 * @author Barry
 * @date 2021-02-01
 */
@Data
@NoArgsConstructor
@ApiModel
public class SysCacheRefreshVO {
    public SysCacheRefreshVO(SysCacheRefresh refresh) {
        this.type = refresh.getType();
        this.finishUpdateTime = refresh.getFinishUpdateTime();
    }

    public SysCacheRefreshVO(String type, SysCacheRefreshBO refresh) {
        this.type = SysCacheRefreshTypeEnum.valueOf(type);
        this.finishUpdateTime = refresh.getFinishUpdateTime();
    }

    @ApiModelProperty(value = "类型", position = 1)
    private SysCacheRefreshTypeEnum type;

    @ApiModelProperty(value = "上次刷新时间，只要跟上次不同就刷新", position = 2)
    private LocalDateTime finishUpdateTime;
}
