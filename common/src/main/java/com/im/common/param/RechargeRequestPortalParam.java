package com.im.common.param;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.entity.enums.RechargeConfigSourceEnum;
import com.im.common.util.NumberUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 充值
 *
 * @author Barry
 * @date 2020-05-26
 */
@Data
@NoArgsConstructor
@ApiModel
public class RechargeRequestPortalParam {
    @NotNull
    @ApiModelProperty(value = "配置来源", required = true, position = 1)
    private RechargeConfigSourceEnum configSource;

    @NotNull
    @ApiModelProperty(value = "配置ID", required = true, position = 2)
    private Long configId;

    @NotNull
    @ApiModelProperty(value = "金额", position = 3)
    private BigDecimal amount;

    @ApiModelProperty(value = "付款人，要看配置是否要求必填", position = 4)
    private String userCardName;

    /**
     * 原单位，1人民币，1美元，还是1什么什么币
     *
     * @return
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false, deserialize = false)
    public String amountStr() {
        return NumberUtil.toStr(amount);
    }

    /**
     * 单位分
     *
     * @return
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false, deserialize = false)
    public String amountFenLongStr() {
        return NumberUtil.amountYuanToFenLongStr(amount);
    }
}
