package com.im.common.vo;

import com.im.common.entity.RechargeOrderLog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDateTime;

/**
 * 充值订单日志VO
 *
 * @author Barry
 * @date 2021-03-24
 */
@Data
@NoArgsConstructor
@ApiModel
public class RechargeOrderLogVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(RechargeOrderLog.class, RechargeOrderLogVO.class, false);

    public RechargeOrderLogVO(RechargeOrderLog e) {
        BEAN_COPIER.copy(e, this, null);
    }

    @ApiModelProperty(value = "ID，不显示", position = 1)
    private Long id;

    @ApiModelProperty(value = "时间", position = 2)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "内容", position = 3)
    private String content;
}
