package com.im.common.cache.sysconfig.vo;

import com.im.common.cache.sysconfig.bo.BaseSysConfigBO;
import com.im.common.cache.sysconfig.bo.RedEnvelopeConfigBO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

/**
 * 红包配置
 *
 * @author Barry
 * @date 2021-01-18
 */
@Data
@NoArgsConstructor
@ApiModel
public class RedEnvelopeConfigPortalVO extends BaseSysConfigBO {
    private static final BeanCopier BEAN_COPIER = BeanCopier.create(RedEnvelopeConfigBO.class, RedEnvelopeConfigPortalVO.class, false);

    public RedEnvelopeConfigPortalVO(RedEnvelopeConfigBO config) {
        BEAN_COPIER.copy(config, this, null);
    }

    @ApiModelProperty(value = "红包过期小时，自发送时间起，过期后仍未被领取，将自动退回剩余金额", position = 1)
    private Integer expireHours;

    @ApiModelProperty(value = "个人红包金额范围", position = 2)
    private String personalAmountRange;

    @ApiModelProperty(value = "群红包单个红包平均金额范围，比如发100块/10个红包，单个红包平均值为10块，但如果发0.01块/10个，单个红包平均值为0.001", position = 3)
    private String groupAverageAmountRange;

    @ApiModelProperty(value = "群红包最多拆分个数", position = 3)
    private Integer groupMaxNum;
}
