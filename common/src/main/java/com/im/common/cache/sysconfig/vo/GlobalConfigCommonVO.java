package com.im.common.cache.sysconfig.vo;

import com.im.common.cache.sysconfig.bo.BaseSysConfigBO;
import com.im.common.cache.sysconfig.bo.GlobalConfigBO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

/**
 * 全局配置
 *
 * @author Barry
 * @date 2018/6/8
 */
@Data
@NoArgsConstructor
@ApiModel
public class GlobalConfigCommonVO extends BaseSysConfigBO {
    private static final BeanCopier BEAN_COPIER = BeanCopier.create(GlobalConfigBO.class, GlobalConfigCommonVO.class, false);

    public GlobalConfigCommonVO(GlobalConfigBO globalConfig) {
        BEAN_COPIER.copy(globalConfig, this, null);
    }

    @ApiModelProperty(value = "默认国际化", position = 1)
    private String defaultI18n;
}
