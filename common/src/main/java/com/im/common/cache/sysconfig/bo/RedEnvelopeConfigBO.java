package com.im.common.cache.sysconfig.bo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 红包配置
 *
 * @author Barry
 * @date 2021-01-18
 */
@Data
@NoArgsConstructor
public class RedEnvelopeConfigBO extends BaseSysConfigBO {
    /**
     * 红包过期小时，自发送时间起，过期后仍未被领取，将自动退回剩余金额给发送人，必须大于0，也不能为0
     */
    private Integer expireHours;

    /**
     * 个人红包金额范围
     */
    private String personalAmountRange;

    /**
     * 群红包单个红包平均金额范围，比如发100块/10个红包，单个红包平均值为10块，但如果发0.01块/10个，单个红包平均值为0.001
     */
    private String groupAverageAmountRange;

    /**
     * 群红包最多拆分个数
     */
    private Integer groupMaxNum;
}
