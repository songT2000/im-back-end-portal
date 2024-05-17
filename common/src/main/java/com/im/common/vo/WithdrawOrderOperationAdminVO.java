package com.im.common.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.im.common.cache.sysconfig.bo.WithdrawConfigBO;
import com.im.common.entity.enums.WithdrawOrderStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 兑换提现订单
 *
 * @author Barry
 * @date 2021-08-28
 */
@Data
@NoArgsConstructor
@ApiModel
public class WithdrawOrderOperationAdminVO {
    /**
     * 初始化各个按钮
     *
     * @param sessionUser
     * @param withdrawConfig
     * @param status
     * @param approveAdminId
     * @param payAdminId
     */
    protected void initPermissions(AdminSessionUser sessionUser,
                                   WithdrawConfigBO withdrawConfig,
                                   WithdrawOrderStatusEnum status,
                                   Long approveAdminId,
                                   Long payAdminId) {
        this.allowApproveLock = false;
        this.allowApproveUnlock = false;
        this.allowApprove = false;
        this.allowPayLock = false;
        this.allowPayUnlock = false;
        this.allowPay = false;
        this.allowPayFinish = false;

        if (Boolean.TRUE.equals(withdrawConfig.getNeedApprove())) {
            // 审核锁定
            if (status == WithdrawOrderStatusEnum.WAITING && approveAdminId == null) {
                // 待处理未锁定可以锁定
                this.allowApproveLock = true;
            } else if (status == WithdrawOrderStatusEnum.WAITING && sessionUser.getId().equals(approveAdminId)) {
                // 待处理已锁定可以再次锁定，但不会有任何修改
                this.allowApproveLock = true;
            }

            // 审核解锁&审核
            if (status == WithdrawOrderStatusEnum.WAITING && sessionUser.getId().equals(approveAdminId)) {
                // 待处理已锁定可以解锁和审核
                this.allowApproveUnlock = true;
                this.allowApprove = true;
            }
        }

        // 打款
        {
            // 打款锁定
            if (status == WithdrawOrderStatusEnum.APPROVE_SUCCESS && payAdminId == null) {
                // 审核通过未锁定可以锁定
                this.allowPayLock = true;
            } else if (status == WithdrawOrderStatusEnum.PAYING && payAdminId == null) {
                // 打款中未锁定可以锁定
                this.allowPayLock = true;
            } else if (status == WithdrawOrderStatusEnum.PAYING && sessionUser.getId().equals(payAdminId)) {
                // 打款中已锁定可以再次锁定，但不会有任何修改
                // 修改为不可以再继续锁定，因为打款中变成打款中，会再次显示一个打款按钮，显得很别扭
                this.allowPayLock = false;
            } else if (status == WithdrawOrderStatusEnum.APPROVE_SUCCESS && sessionUser.getId().equals(payAdminId)) {
                // 审核通过已锁定可以再次锁定，但不会有任何修改
                this.allowPayLock = true;
            } else if (!Boolean.TRUE.equals(withdrawConfig.getNeedApprove()) && status == WithdrawOrderStatusEnum.WAITING && payAdminId == null) {
                // 不需要审核待处理未锁定可以锁定
                this.allowPayLock = true;
            } else if (!Boolean.TRUE.equals(withdrawConfig.getNeedApprove()) && status == WithdrawOrderStatusEnum.WAITING && sessionUser.getId().equals(payAdminId)) {
                // 不需要审核待处理已锁定可以再次锁定，但不会有任何修改
                this.allowPayLock = true;
            }

            // 打款解锁
            if (status == WithdrawOrderStatusEnum.WAITING && sessionUser.getId().equals(payAdminId)) {
                // 待处理已锁定可以解锁
                this.allowPayUnlock = true;
            } else if (status == WithdrawOrderStatusEnum.APPROVE_SUCCESS && sessionUser.getId().equals(payAdminId)) {
                // 审核成功已锁定可以解锁
                this.allowPayUnlock = true;
            } else if (status == WithdrawOrderStatusEnum.PAYING && sessionUser.getId().equals(payAdminId)) {
                // 打款中已锁定可以解锁
                this.allowPayUnlock = true;
            }

            // 打款
            if (status == WithdrawOrderStatusEnum.APPROVE_SUCCESS && sessionUser.getId().equals(payAdminId)) {
                // 审核通过已锁定可以打款
                this.allowPay = true;
            } else if (status == WithdrawOrderStatusEnum.WAITING && sessionUser.getId().equals(payAdminId)) {
                // 待处理已锁定可以打款
                this.allowPay = true;
            }

            // 到账
            if (status == WithdrawOrderStatusEnum.PAYING && sessionUser.getId().equals(payAdminId)) {
                // 打款中已锁定可以到账
                this.allowPayFinish = true;
            }
        }
    }

    @ApiModelProperty(value = "是否显示审核锁定", position = 38)
    @ExcelIgnore
    protected Boolean allowApproveLock;

    @ApiModelProperty(value = "是否显示审核解锁", position = 39)
    @ExcelIgnore
    protected Boolean allowApproveUnlock;

    @ApiModelProperty(value = "是否显示审核通过和审核拒绝", position = 40)
    @ExcelIgnore
    protected Boolean allowApprove;

    @ApiModelProperty(value = "是否显示打款锁定", position = 41)
    @ExcelIgnore
    protected Boolean allowPayLock;

    @ApiModelProperty(value = "是否显示打款解锁", position = 42)
    @ExcelIgnore
    protected Boolean allowPayUnlock;

    @ApiModelProperty(value = "是否显示打款", position = 43)
    @ExcelIgnore
    protected Boolean allowPay;

    @ApiModelProperty(value = "是否显示到账", position = 44)
    @ExcelIgnore
    protected Boolean allowPayFinish;
}
