package com.im.common.vo;

import com.im.common.entity.Bank;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.cglib.beans.BeanCopier;

/**
 * 银行简单VO
 *
 * @author Barry
 * @date 2020-05-27
 */
@Data
@ApiModel
public class BankSimpleVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(Bank.class, BankSimpleVO.class, false);

    public BankSimpleVO(Bank bank) {
        BEAN_COPIER.copy(bank, this, null);
    }

    @ApiModelProperty(value = "ID", position = 1)
    private Long id;

    @ApiModelProperty(value = "名称", position = 2)
    private String name;

    @ApiModelProperty(value = "编码", position = 3)
    private String code;
}
