package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 参数
 *
 * @author Barry
 * @date 2020-07-11
 */
@Data
@NoArgsConstructor
@ApiModel
public class DateRangeParam {
    @NotNull
    @ApiModelProperty(value = "开始日期，yyyy-MM-dd", required = true)
    protected LocalDate startDate;

    @NotNull
    @ApiModelProperty(value = "结束日期，yyyy-MM-dd", required = true)
    protected LocalDate endDate;
}
