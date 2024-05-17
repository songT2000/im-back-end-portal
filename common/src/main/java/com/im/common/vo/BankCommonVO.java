package com.im.common.vo;

import com.im.common.entity.Bank;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.cglib.beans.BeanCopier;

/**
 * 银行VO
 *
 * @author Barry
 * @date 2020-05-27
 */
@Data
@ApiModel
public class BankCommonVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(Bank.class, BankCommonVO.class, false);

    public BankCommonVO(Bank bank) {
        BEAN_COPIER.copy(bank, this, null);
    }

    @ApiModelProperty(value = "ID", position = 1)
    private Long id;

    @ApiModelProperty(value = "名称", position = 2)
    private String name;

    @ApiModelProperty(value = "编码", position = 3)
    private String code;

    @ApiModelProperty(value = "排序号，服务器已排序，可忽略", position = 4)
    private Integer sort;

    @ApiModelProperty(value = "是否启用提现", position = 5)
    private Boolean withdrawEnabled;
}
