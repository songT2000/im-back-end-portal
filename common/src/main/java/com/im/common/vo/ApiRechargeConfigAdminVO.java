package com.im.common.vo;

import com.im.common.entity.ApiRechargeConfig;
import com.im.common.entity.enums.ApiRechargeConfigCodeEnum;
import com.im.common.entity.enums.RechargeConfigGroupEnum;
import com.im.common.util.NumberUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.math.BigDecimal;

/**
 * 三方充值配置
 * @author max.stark
 */
@Data
@NoArgsConstructor
@ApiModel
public class ApiRechargeConfigAdminVO {

    public static final BeanCopier BEAN_COPIER = BeanCopier.create(ApiRechargeConfig.class, ApiRechargeConfigAdminVO.class, false);

    /**
     * @param e  不能为空
     */
    public ApiRechargeConfigAdminVO(ApiRechargeConfig e) {
        BEAN_COPIER.copy(e, this, null);

        this.serviceChargePercentStr = NumberUtil.pointToStr(e.getServiceChargePercent());
    }

    @ApiModelProperty(value = "Id", position = 1)
    private Long id;

    @ApiModelProperty(value = "分组，只是用来做前台展示归类，不影响逻辑", position = 2)
    private RechargeConfigGroupEnum group;

    @ApiModelProperty(value = "后台名称", position = 3)
    private String adminName;

    @ApiModelProperty(value = "前台名称", position = 4)
    private String portalName;

    @ApiModelProperty(value = "编码，每种编码都有不同的处理类", position = 5)
    private ApiRechargeConfigCodeEnum code;

    @ApiModelProperty(value = "金额范围", position = 6)
    private String amountRange;

    @ApiModelProperty(value = "金额最多小数位", position = 7)
    private Integer amountMaxPrecision;

    @ApiModelProperty(value = "启用时间段，格式HH:mm:ss~HH:mm:ss", position = 8)
    private String enableTime;

    @ApiModelProperty(value = "手续费百分比，0.01就是%1", position = 9)
    private BigDecimal serviceChargePercent;

    @ApiModelProperty(value = "手续费百分比，0.01就是%1", position = 10)
    private String serviceChargePercentStr;

    @ApiModelProperty(value = "是否需要输入付款人，针对像卡转卡这种", position = 11)
    private Boolean needInputUserCardName;

    @ApiModelProperty(value = "三方配置，密钥，URL什么的，JSON格式", position = 12)
    private String thirdConfig;

    @ApiModelProperty(value = "三方回调IP白名单，多个用英文逗号分隔", position = 13)
    private String thirdCallbackWhitelistIp;

    @ApiModelProperty(value = "排序号，越小排越前面", position = 14)
    private Integer sort;

    @ApiModelProperty(value = "删除", position = 15)
    private Boolean deleted;

    @ApiModelProperty(value = "启用/禁用", position = 16)
    private Boolean enabled;
}
