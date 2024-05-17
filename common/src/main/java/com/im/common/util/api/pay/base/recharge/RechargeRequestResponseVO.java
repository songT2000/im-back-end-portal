package com.im.common.util.api.pay.base.recharge;

import com.im.common.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * 请求充值返回
 *
 * @author Barry
 * @date 2021-03-21
 */
@Data
@ApiModel
public class RechargeRequestResponseVO {
    public RechargeRequestResponseVO(Long orderId, RechargeResponseDataTypeEnum dataType, Object data) {
        this.orderId = orderId;
        this.dataType = dataType;
        this.data = data;
    }

    @ApiModelProperty(value = "订单ID", position = 1)
    private Long orderId;

    @ApiModelProperty(value = "数据类型，各个数据类型可以在本目录菜单中查看说明", position = 2)
    private RechargeResponseDataTypeEnum dataType;

    @ApiModelProperty(value = "数据，根据dataType而不同，详参考RechargeResponseDataTypeEnum里面每个枚举说明", position = 3)
    private Object data;

    /**
     * 跳转链接
     *
     * @param orderId
     * @param url     跳转链接
     * @return
     */
    public static RechargeRequestResponseVO url(Long orderId, String url) {
        RechargeRequestResponseVO rsp = new RechargeRequestResponseVO(orderId, RechargeResponseDataTypeEnum.URL, url);
        return rsp;
    }

    /**
     * FORM表单
     *
     * @param orderId
     * @param method
     * @param action
     * @param param
     * @return
     */
    public static RechargeRequestResponseVO form(Long orderId, String method, String action, Map<String, String> param) {
        RechargeResponseForm form = new RechargeResponseForm(method, action, param);
        RechargeRequestResponseVO rsp = new RechargeRequestResponseVO(orderId, RechargeResponseDataTypeEnum.FORM, form);
        return rsp;
    }

    /**
     * 返回银行卡信息
     *
     * @param orderId
     * @param bankName 不能为空
     * @param cardName 不能为空
     * @param cardNum  不能为空
     * @param branch   可以为空
     * @return
     */
    public static RechargeRequestResponseVO bankCard(Long orderId, String bankName, String cardName, String cardNum, String branch) {
        RechargeResponseBankCard bankCard = new RechargeResponseBankCard(bankName, cardName, cardNum, StrUtil.blankToDefault(branch, bankName));
        RechargeRequestResponseVO rsp = new RechargeRequestResponseVO(orderId, RechargeResponseDataTypeEnum.BANK_CARD, bankCard);
        return rsp;
    }
}
