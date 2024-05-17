package com.im.common.util.api.pay.base.recharge;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Barry
 * @date 2021-03-22
 */
@Data
@NoArgsConstructor
@ApiModel
public class RechargeResponseBankCard {
    public RechargeResponseBankCard(String bankName, String cardName, String cardNum, String branch) {
        this.bankName = bankName;
        this.cardName = cardName;
        this.cardNum = cardNum;
        this.branch = branch;
    }

    public RechargeResponseBankCard(Long bankId, String bankName, String cardName, String cardNum, String branch) {
        this(bankName, cardName, cardNum, branch);
        this.bankId = bankId;
    }

    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false)
    private Long bankId;

    @ApiModelProperty(value = "银行名", position = 1)
    private String bankName;

    @ApiModelProperty(value = "卡姓名", position = 2)
    private String cardName;

    @ApiModelProperty(value = "卡号", position = 3)
    private String cardNum;

    @ApiModelProperty(value = "支行，可能是空数据，注意处理，没有数据的话，就不用展示支行这一行", position = 4)
    private String branch;
}
