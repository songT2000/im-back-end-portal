package com.im.common.cache.sysconfig.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.util.DateTimeUtil;
import com.im.common.util.StrUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.beans.Transient;

/**
 * 提现配置
 *
 * @author Barry
 * @date 2021-01-18
 */
@Data
@NoArgsConstructor
public class WithdrawConfigBO extends BaseSysConfigBO {
    /**
     * 是否允许提现，禁止后所有用户都不能提交新的提现订单
     */
    private Boolean enabled;

    /**
     * 启用时间段，HH:mm:ss~HH:mm:ss，00:00:00~00:00:00表示24小时都允许，为空表示24小时都允许
     */
    private String enableTime;

    /**
     * 每日最多提交笔数，只计算打款完成和处理中的，每日0点清除，小于等于0不限制
     */
    private Integer maxDailyRequest;

    /**
     * 同时最大提现笔数，同时最多允许提交几笔，小于等于0不限制
     */
    private Integer maxSameTimeRequest;

    /**
     * 是否需要审核步骤，用户提交提现订单后，是否需要审核步骤
     */
    private Boolean needApprove;

    /**
     * 手动打款是否直接完成订单，处理用户提现订单时，手动打款是否直接完成订单，还是走一个打款中的中间状态
     */
    private Boolean manualPayDirectSuccess;

    /**
     * 最多绑定银行卡数，每个用户最多绑定银行卡数量，包含启用和禁用的，小于等于0不限制
     */
    private Integer maxBindBankCardCount;

    /**
     * 当前时间是否在可用时间段内
     *
     * @return boolean
     */
    @JSONField(serialize = false)
    @Transient
    public boolean isDuringEnableTime() {
        if (StrUtil.isBlank(enableTime)) {
            return true;
        }

        return DateTimeUtil.isDuringServiceTime(enableTime);
    }
}
