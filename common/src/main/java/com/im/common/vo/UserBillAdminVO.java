package com.im.common.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.im.common.entity.UserBill;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.enums.UserBillTypeEnum;
import com.im.common.util.easyexcel.EasyexcelEnumConverter;
import com.im.common.util.easyexcel.EasyexcelLocalDateTimeConverter;
import com.im.common.util.user.UserUtil;
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
@ColumnWidth(20)
public class UserBillAdminVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(UserBill.class, UserBillAdminVO.class, false);

    public UserBillAdminVO(UserBill bill) {
        BEAN_COPIER.copy(bill, this, null);

        this.username = UserUtil.getUsernameByIdFromLocal(bill.getUserId(), PortalTypeEnum.PORTAL);
    }

    @ApiModelProperty(value = "ID", position = 1)
    @ExcelIgnore
    private Long id;

    @ApiModelProperty(value = "用户ID", position = 2)
    @ExcelIgnore
    private Long userId;

    @ApiModelProperty(value = "用户名", position = 3)
    @ExcelProperty(value = "用户名", index = 0)
    private String username;

    @ApiModelProperty(value = "类型", position = 4)
    @ExcelProperty(value = "类型", index = 1, converter = EasyexcelEnumConverter.class)
    private UserBillTypeEnum type;

    @ApiModelProperty(value = "金额", position = 5)
    @ExcelProperty(value = "金额", index = 2)
    private BigDecimal amount;

    @ApiModelProperty(value = "余额", position = 6)
    @ExcelProperty(value = "余额", index = 3)
    private BigDecimal balance;

    @ApiModelProperty(value = "订单号", position = 7)
    @ExcelProperty(value = "订单号", index = 4)
    private String orderNum;

    @ApiModelProperty(value = "账变时间", position = 8)
    @ExcelProperty(value = "账变时间", index = 5, converter = EasyexcelLocalDateTimeConverter.class)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "记账日", position = 9)
    @ExcelProperty(value = "记账日", index = 6)
    private String reportDate;

    @ApiModelProperty(value = "备注", position = 10)
    @ExcelProperty(value = "备注", index = 7)
    private String remark;
}
