package com.im.common.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.im.common.cache.impl.ApiWithdrawConfigCache;
import com.im.common.cache.impl.BankCache;
import com.im.common.cache.impl.BankCardWithdrawConfigCache;
import com.im.common.cache.sysconfig.bo.WithdrawConfigBO;
import com.im.common.entity.WithdrawOrder;
import com.im.common.entity.enums.*;
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
 * 提现订单
 *
 * @author Barry
 * @date 2021-08-28
 */
@Data
@NoArgsConstructor
@ApiModel
@ColumnWidth(20)
public class WithdrawOrderAdminVO extends WithdrawOrderOperationAdminVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(WithdrawOrder.class, WithdrawOrderAdminVO.class, false);

    public WithdrawOrderAdminVO(WithdrawOrder e,
                                BankCardWithdrawConfigCache bankCardWithdrawConfigCache,
                                BankCache bankCache,
                                ApiWithdrawConfigCache apiWithdrawConfigCache,
                                AdminSessionUser sessionUser,
                                WithdrawConfigBO withdrawConfig) {
        BEAN_COPIER.copy(e, this, null);

        this.username = UserUtil.getUsernameByIdFromLocal(e.getUserId(), PortalTypeEnum.PORTAL);

        if (e.getRequestWithdrawConfigId() != null) {
            if (e.getRequestWithdrawConfigSource() == WithdrawConfigSourceEnum.BANK_CARD_WITHDRAW_CONFIG) {
                this.requestWithdrawConfigName = bankCardWithdrawConfigCache == null ? null : bankCardWithdrawConfigCache.getNameByIdFromLocal(e.getRequestWithdrawConfigId());
            }
        }

        if (e.getUserBankId() != null) {
            this.userBankName = bankCache == null ? null : bankCache.getNameByIdFromLocal(e.getUserBankId());
        }

        if (e.getApiWithdrawConfigId() != null) {
            if (e.getPayType() == WithdrawOrderPayTypeEnum.API_PAY) {
                this.apiWithdrawConfigName = apiWithdrawConfigCache == null ? null : apiWithdrawConfigCache.getNameByIdFromLocal(e.getApiWithdrawConfigId());
            }
        }

        this.approveAdminUsername = e.getApproveAdminId() == null ? null : UserUtil.getUsernameByIdFromLocal(e.getApproveAdminId(), PortalTypeEnum.ADMIN);
        this.payAdminUsername = e.getPayAdminId() == null ? null : UserUtil.getUsernameByIdFromLocal(e.getPayAdminId(), PortalTypeEnum.ADMIN);

        // 初始化各个按钮
        initPermissions(sessionUser, withdrawConfig, e.getStatus(), e.getApproveAdminId(), e.getPayAdminId());
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

    @ApiModelProperty(value = "到账金额", position = 5)
    @ExcelProperty(value = "到账金额", index = 3)
    private BigDecimal receiveAmount;

    @ApiModelProperty(value = "手续费比例", position = 6)
    @ExcelIgnore
    private BigDecimal serviceChargePercent;

    @ApiModelProperty(value = "手续费比例", position = 7)
    @ExcelIgnore
    private String serviceChargePercentStr;

    @ApiModelProperty(value = "手续费", position = 8)
    @ExcelProperty(value = "手续费", index = 4)
    private BigDecimal serviceCharge;

    @ApiModelProperty(value = "状态", position = 9)
    @ExcelProperty(value = "状态", index = 5, converter = EasyexcelEnumConverter.class)
    private WithdrawOrderStatusEnum status;

    @ApiModelProperty(value = "提现方式", position = 10)
    @ExcelProperty(value = "提现方式", index = 6, converter = EasyexcelEnumConverter.class)
    private WithdrawOrderTypeEnum type;

    @ApiModelProperty(value = "提单时间", position = 11)
    @ExcelProperty(value = "提单时间", index = 7, converter = EasyexcelLocalDateTimeConverter.class)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "打款完成时间", position = 12)
    @ExcelProperty(value = "打款完成时间", index = 8, converter = EasyexcelLocalDateTimeConverter.class)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payFinishTime;

    @ApiModelProperty(value = "提现配置来源", position = 13)
    @ExcelIgnore
    private WithdrawConfigSourceEnum requestWithdrawConfigSource;

    @ApiModelProperty(value = "提现配置ID", position = 14)
    @ExcelIgnore
    private Long requestWithdrawConfigId;

    @ApiModelProperty(value = "提现配置名", position = 15)
    @ExcelProperty(value = "提现配置名", index = 9)
    private String requestWithdrawConfigName;

    @ApiModelProperty(value = "银行ID", position = 16)
    @ExcelIgnore
    private Long userBankId;

    @ApiModelProperty(value = "收款银行", position = 17)
    @ExcelProperty(value = "收款银行", index = 10)
    private String userBankName;

    @ApiModelProperty(value = "收款卡姓名", position = 18)
    @ExcelProperty(value = "收款卡姓名", index = 11)
    private String userBankCardName;

    @ApiModelProperty(value = "收款卡号", position = 19)
    @ExcelProperty(value = "收款卡号", index = 12)
    private String userBankCardNum;

    @ApiModelProperty(value = "收款卡支行", position = 20)
    @ExcelProperty(value = "收款卡支行", index = 13)
    private String userBankCardBranch;

    @ApiModelProperty(value = "审核人", position = 21)
    @ExcelProperty(value = "审核人", index = 14)
    private String approveAdminUsername;

    @ApiModelProperty(value = "审核时间", position = 22)
    @ExcelProperty(value = "审核时间", index = 15, converter = EasyexcelLocalDateTimeConverter.class)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approveTime;

    @ApiModelProperty(value = "打款人", position = 23)
    @ExcelProperty(value = "打款人", index = 16)
    private String payAdminUsername;

    @ApiModelProperty(value = "打款提交时间", position = 24)
    @ExcelProperty(value = "打款提交时间", index = 17, converter = EasyexcelLocalDateTimeConverter.class)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payRequestTime;

    @ApiModelProperty(value = "备注", position = 25)
    @ExcelProperty(value = "备注", index = 18)
    private String remark;

    @ApiModelProperty(value = "记账日", position = 26)
    @ExcelProperty(value = "记账日", index = 19)
    private String reportDate;

    @ApiModelProperty(value = "请求IP，客人提交的单就是客人的IP，管理员提交的单就是管理员的IP", position = 27)
    @ExcelProperty(value = "请求IP", index = 20)
    private String requestIp;

    @ApiModelProperty(value = "打款方式", position = 28)
    @ExcelProperty(value = "打款方式", index = 21, converter = EasyexcelEnumConverter.class)
    private WithdrawOrderPayTypeEnum payType;

    @ApiModelProperty(value = "代付配置ID", position = 29)
    @ExcelIgnore
    private Long apiWithdrawConfigId;

    @ApiModelProperty(value = "代付配置名", position = 30)
    @ExcelProperty(value = "代付配置名", index = 22)
    private String apiWithdrawConfigName;

    @ApiModelProperty(value = "订单完成方式", position = 31)
    @ExcelProperty(value = "订单完成方式", index = 23, converter = EasyexcelEnumConverter.class)
    private WithdrawOrderFinishTypeEnum finishType;

    @ApiModelProperty(value = "三方回调IP", position = 32)
    @ExcelProperty(value = "三方回调IP", index = 24)
    private String apiWithdrawCallbackIp;
}
