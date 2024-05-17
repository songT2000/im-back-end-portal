package com.im.common.cache.sysconfig.vo;

import com.im.common.cache.sysconfig.bo.BaseSysConfigBO;
import com.im.common.entity.enums.SysConfigGroupEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统配置VO
 *
 * @author Daniel
 * @date 2019/10/25
 */
@Data
@NoArgsConstructor
@ApiModel
public class SysConfigVO {
    @ApiModelProperty("组")
    private SysConfigGroupEnum group;

    @ApiModelProperty("配置对象，对象字段不固定，各种配置都不同")
    private BaseSysConfigBO config;

    public SysConfigVO(SysConfigGroupEnum group, BaseSysConfigBO config) {
        this.group = group;
        this.config = config;
    }
}
