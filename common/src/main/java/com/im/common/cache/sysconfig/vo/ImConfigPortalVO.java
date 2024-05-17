package com.im.common.cache.sysconfig.vo;

import com.im.common.cache.sysconfig.bo.BaseSysConfigBO;
import com.im.common.cache.sysconfig.bo.ImConfigBO;
import com.im.common.cache.sysconfig.bo.PortalConfigBO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

/**
 * IM配置
 *
 * @author Barry
 * @date 2021-01-18
 */
@Data
@NoArgsConstructor
@ApiModel
public class ImConfigPortalVO extends BaseSysConfigBO {
    private static final BeanCopier BEAN_COPIER = BeanCopier.create(ImConfigBO.class, ImConfigPortalVO.class, false);

    public ImConfigPortalVO(ImConfigBO config) {
        BEAN_COPIER.copy(config, this, null);
    }

    @ApiModelProperty(value = "腾讯IM SDK APPID", position = 1)
    private Long tecentImSdkAppid;
}
