package com.im.common.cache.sysconfig.vo;

import com.im.common.cache.sysconfig.bo.BaseSysConfigBO;
import com.im.common.cache.sysconfig.bo.PortalConfigBO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

/**
 * 前台配置
 *
 * @author Barry
 * @date 2021-01-18
 */
@Data
@NoArgsConstructor
@ApiModel
public class PortalConfigPortalVO extends BaseSysConfigBO {
    private static final BeanCopier BEAN_COPIER = BeanCopier.create(PortalConfigBO.class, PortalConfigPortalVO.class, false);

    public PortalConfigPortalVO(PortalConfigBO config) {
        BEAN_COPIER.copy(config, this, null);
    }

    /**
     * 免登录分钟，一次登录，后续多久时间内无需要再次登录，30~99999999
     **/
    @ApiModelProperty(value = "免登录分钟",position = 1)
    private Long loginExpireMinutes;

    /**
     * 是否允许WEB/手机同时登录，每种类型只允许同时登录一个，如果允许，则IM修改为双平台登录，登录个数都是1；如果不允许，则IM修改为单平台登录，登录个数都是1；
     **/
    @ApiModelProperty(value = "是否允许WEB/手机同时登录",position = 2)
    private Boolean allowWebMobileSameLogin;

    /**
     * 显示在app和web首页应用的名称，比如“酷聊”，“速聊”
     */
    @ApiModelProperty(value = "应用名称",position = 3)
    private String systemName;
    /**
     * 显示在app和web首页应用的icon地址
     */
    @ApiModelProperty(value = "应用icon地址",position = 4)
    private String iconUrl;
}
