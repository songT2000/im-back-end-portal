package com.im.common.vo;

import com.im.common.entity.ApiWithdrawConfig;
import com.im.common.entity.enums.ApiWithdrawConfigCodeEnum;
import com.im.common.entity.enums.WithdrawConfigSourceEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

/**
 * 三方提现配置
 *
 * @author max.stark
 */
@Data
@NoArgsConstructor
@ApiModel
public class ApiWithdrawConfigAdminVO {

    public static final BeanCopier BEAN_COPIER = BeanCopier.create(ApiWithdrawConfig.class, ApiWithdrawConfigAdminVO.class, false);

    public ApiWithdrawConfigAdminVO(ApiWithdrawConfig e) {
        BEAN_COPIER.copy(e, this, null);
    }

    @ApiModelProperty(value = "id", position = 1)
    private Long id;

    @ApiModelProperty(value = "名称", position = 2)
    private String name;

    @ApiModelProperty(value = "编码，每种编码都有不同的处理类", position = 3)
    private ApiWithdrawConfigCodeEnum code;

    @ApiModelProperty(value = "适用提现方式", position = 4)
    private WithdrawConfigSourceEnum withdrawConfigSource;

    @ApiModelProperty(value = "三方配置，密钥，URL什么的，JSON格式", position = 5)
    private String thirdConfig;

    @ApiModelProperty(value = "三方回调IP白名单", position = 6)
    private String thirdCallbackWhitelistIp;

    @ApiModelProperty(value = "排序号，越小排越前面", position = 7)
    private Integer sort;

    @ApiModelProperty(value = "启用/禁用", position = 8)
    private Boolean enabled;

    @ApiModelProperty(value = "删除", position = 9)
    private Boolean deleted;
}
