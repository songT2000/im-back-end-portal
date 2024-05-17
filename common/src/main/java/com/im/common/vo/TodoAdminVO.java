package com.im.common.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 管理后台待办事项
 *
 * @author Barry
 * @date 2020-08-05
 */
@Data
@NoArgsConstructor
@ApiModel
public class TodoAdminVO {
    // @ApiModelProperty(value = "供应商银行卡流水未匹配", position = 1)
    // private Integer supplierBankCardBillUnMatchCount;
    //
    // public void setSupplierBankCardBillData(TodoAdminVO vo) {
    //     TodoAdminVO data = Optional.ofNullable(vo).orElse(new TodoAdminVO());
    //
    //     this.supplierBankCardBillUnMatchCount = NumberUtil.nullOrZero(data.getSupplierBankCardBillUnMatchCount());
    // }
}
