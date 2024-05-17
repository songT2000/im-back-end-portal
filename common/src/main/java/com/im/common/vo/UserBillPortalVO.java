package com.im.common.vo;

import com.im.common.entity.UserBill;
import com.im.common.entity.enums.UserBillTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 账变VO
 *
 * @author Barry
 * @date 2020-05-27
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserBillPortalVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(UserBill.class, UserBillPortalVO.class, false);

    public UserBillPortalVO(UserBill bill) {
        BEAN_COPIER.copy(bill, this, null);
    }

    @ApiModelProperty(value = "ID", position = 1)
    private Long id;

    @ApiModelProperty(value = "类型", position = 2)
    private UserBillTypeEnum type;

    @ApiModelProperty(value = "金额", position = 3)
    private BigDecimal amount;

    @ApiModelProperty(value = "余额", position = 4)
    private BigDecimal balance;

    @ApiModelProperty(value = "账变时间", position = 5)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "备注", position = 6)
    private String remark;
}
