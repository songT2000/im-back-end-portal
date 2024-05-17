package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 发送群红包
 *
 * @author Barry
 * @date 2019-11-20
 */
@Data
@NoArgsConstructor
@ApiModel
public class GroupRedEnvelopeSendPortalParam {
    @NotBlank
    @ApiModelProperty(value = "群ID", required = true, position = 1)
    private String groupId;

    @NotNull
    @ApiModelProperty(value = "金额", required = true, position = 2)
    private BigDecimal amount;

    @NotNull
    @ApiModelProperty(value = "个数", required = true, position = 3)
    private Integer num;

    @ApiModelProperty(value = "备注，显示在红包上的文字", position = 4)
    private String remark;
}
