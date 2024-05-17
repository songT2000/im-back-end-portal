package com.im.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.im.common.entity.base.BaseDeletableEnableEntity;
import com.im.common.entity.enums.ApiRechargeConfigCodeEnum;
import com.im.common.entity.enums.RechargeConfigGroupEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 三方充值配置
 *
 * @author Barry
 * @date 2021-09-29
 */
@Data
@NoArgsConstructor
public class ApiRechargeConfig extends BaseDeletableEnableEntity implements Serializable {
    private static final long serialVersionUID = 297994590758056527L;

    /**
     * 分组，只是用来做前台展示归类，不影响逻辑
     **/
    @TableField("`group`")
    private RechargeConfigGroupEnum group;

    /**
     * 后台名称
     **/
    private String adminName;

    /**
     * 前台名称，PC端/H5等
     **/
    private String portalName;

    /**
     * 编码，每种编码都有不同的处理类
     **/
    private ApiRechargeConfigCodeEnum code;

    /**
     * 金额范围，100~30000为任意值，100,200为选项值，100为最小值且无上限
     */
    private String amountRange;

    /**
     * 金额最多小数位
     */
    private Integer amountMaxPrecision;

    /**
     * 启用时间段，格式HH:mm:ss~HH:mm:ss，为空或闭环为全天可用，09:00:00~00:00:00，格式错误全天不可用，为空不生效
     */
    private String enableTime;

    /**
     * 手续费百分比，0.01就是%1
     **/
    private BigDecimal serviceChargePercent;

    /**
     * 是否需要输入付款人，针对像卡转卡这种
     */
    private Boolean needInputUserCardName;

    /**
     * 三方配置，密钥，URL什么的，JSON格式
     */
    private String thirdConfig;

    /**
     * 三方回调IP白名单，多个用英文逗号分隔，支持IPV4/IPV4掩码/IPV6，为空不生效
     */
    private String thirdCallbackWhitelistIp;

    /**
     * 排序号，越小排越前面
     */
    private Integer sort;
}
