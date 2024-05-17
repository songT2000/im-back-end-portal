package com.im.common.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.im.common.cache.impl.ApiRechargeConfigCache;
import com.im.common.cache.impl.BankCache;
import com.im.common.cache.impl.BankCardRechargeConfigCache;
import com.im.common.entity.RechargeOrder;
import com.im.common.entity.enums.*;
import com.im.common.util.NumberUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.easyexcel.EasyexcelEnumConverter;
import com.im.common.util.easyexcel.EasyexcelLocalDateTimeConverter;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 充值订单
 *
 * @author Barry
 * @date 2021-08-28
 */
@Data
@NoArgsConstructor
@ApiModel
@ColumnWidth(20)
public class RechargeOrderAdminVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(RechargeOrder.class, RechargeOrderAdminVO.class, false);

    public RechargeOrderAdminVO(RechargeOrder e,
                                BankCardRechargeConfigCache bankCardRechargeConfigCache,
                                ApiRechargeConfigCache apiRechargeConfigCache,
                                BankCache bankCache) {
        BEAN_COPIER.copy(e, this, null);

        this.username = UserUtil.getUsernameByIdFromLocal(e.getUserId(), PortalTypeEnum.PORTAL);
        this.serviceChargePercentStr = NumberUtil.pointToStr(this.serviceChargePercent);

        if (e.getRechargeConfigId() != null) {
            if (e.getRechargeConfigSource() == RechargeConfigSourceEnum.BANK_CARD_RECHARGE_CONFIG) {
                this.rechargeConfigName = bankCardRechargeConfigCache == null ? null : bankCardRechargeConfigCache.getNameByIdFromLocal(e.getRechargeConfigId());
            } else if (e.getRechargeConfigSource() == RechargeConfigSourceEnum.API_RECHARGE_CONFIG) {
                this.rechargeConfigName = apiRechargeConfigCache == null ? null : apiRechargeConfigCache.getAdminNameByIdFromLocal(e.getRechargeConfigId());
            }
        }

        if (e.getReceiveBankId() != null && StrUtil.isBlank(e.getReceiveBankName())) {
            this.receiveBankName = bankCache == null ? null : bankCache.getNameByIdFromLocal(e.getReceiveBankId());
        }

        this.allowPatch = e.getStatus() == RechargeOrderStatusEnum.WAITING;
    }

    @ApiModelProperty(value = "ID", position = 1)
    @ExcelIgnore
    private Long id;

    @ApiModelProperty(value = "订单号", position = 2)
    @ExcelProperty(value = "订单号", index = 0)
    private String orderNum;

    @ApiModelProperty(value = "用户名", position = 3)
    @ExcelProperty(value = "用户名", index = 1)
    private String username;

    @ApiModelProperty(value = "提单金额", position = 4)
    @ExcelProperty(value = "提单金额", index = 2)
    private BigDecimal requestAmount;

    @ApiModelProperty(value = "实付金额", position = 5)
    @ExcelProperty(value = "实付金额", index = 3)
    private BigDecimal payAmount;

    @ApiModelProperty(value = "到账金额", position = 6)
    @ExcelProperty(value = "到账金额", index = 4)
    private BigDecimal receiveAmount;

    @ApiModelProperty(value = "手续费比例", position = 7)
    @ExcelIgnore
    private BigDecimal serviceChargePercent;

    @ApiModelProperty(value = "手续费比例", position = 8)
    @ExcelIgnore
    private String serviceChargePercentStr;

    @ApiModelProperty(value = "手续费金额", position = 9)
    @ExcelProperty(value = "手续费金额", index = 5)
    private BigDecimal serviceCharge;

    @ApiModelProperty(value = "状态", position = 10)
    @ExcelProperty(value = "状态", index = 6, converter = EasyexcelEnumConverter.class)
    private RechargeOrderStatusEnum status;

    @ApiModelProperty(value = "充值方式", position = 11)
    @ExcelProperty(value = "充值方式", index = 7, converter = EasyexcelEnumConverter.class)
    private RechargeOrderTypeEnum type;

    @ApiModelProperty(value = "配置来源", position = 12)
    @ExcelIgnore
    private RechargeConfigSourceEnum rechargeConfigSource;

    @ApiModelProperty(value = "配置ID", position = 13)
    @ExcelIgnore
    private Long rechargeConfigId;

    @ApiModelProperty(value = "充值配置名", position = 14)
    @ExcelProperty(value = "充值配置名", index = 8)
    private String rechargeConfigName;

    @ApiModelProperty(value = "提单时间", position = 15)
    @ExcelProperty(value = "提单时间", index = 9, converter = EasyexcelLocalDateTimeConverter.class)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "完成时间", position = 16)
    @ExcelProperty(value = "完成时间", index = 10, converter = EasyexcelLocalDateTimeConverter.class)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime finishTime;

    @ApiModelProperty(value = "付款人", position = 17)
    @ExcelProperty(value = "付款人", index = 11)
    private String userCardName;

    @ApiModelProperty(value = "收款银行ID", position = 18)
    @ExcelIgnore
    private Long receiveBankId;

    @ApiModelProperty(value = "收款银行", position = 19)
    @ExcelProperty(value = "收款银行", index = 12)
    private String receiveBankName;

    @ApiModelProperty(value = "收款卡姓名", position = 20)
    @ExcelProperty(value = "收款卡姓名", index = 13)
    private String receiveBankCardName;

    @ApiModelProperty(value = "收款卡号", position = 21)
    @ExcelProperty(value = "收款卡号", index = 14)
    private String receiveBankCardNum;

    @ApiModelProperty(value = "收款卡支行", position = 22)
    @ExcelProperty(value = "收款卡支行", index = 15)
    private String receiveBankCardBranch;

    @ApiModelProperty(value = "备注，仅管理员可见", position = 23)
    @ExcelProperty(value = "备注", index = 16)
    private String remark;

    @ApiModelProperty(value = "记账日", position = 24)
    @ExcelProperty(value = "记账日", index = 17)
    private String reportDate;

    @ApiModelProperty(value = "请求IP，客人提交的单就是客人的IP，管理员提交的单就是管理员的IP", position = 25)
    @ExcelProperty(value = "请求IP", index = 18)
    private String requestIp;

    @ApiModelProperty(value = "订单完成方式", position = 26)
    @ExcelProperty(value = "订单完成方式", index = 19, converter = EasyexcelEnumConverter.class)
    private RechargeOrderFinishTypeEnum finishType;

    @ApiModelProperty(value = "三方回调IP", position = 27)
    @ExcelProperty(value = "三方回调IP", index = 20)
    private String apiRechargeCallbackIp;

    @ApiModelProperty(value = "是否允许补单", position = 28)
    @ExcelIgnore
    private Boolean allowPatch;
}
