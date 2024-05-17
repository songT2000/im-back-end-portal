package com.im.common.util.mybatis.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 日期范围分页参数父类
 *
 * @author Barry
 * @date 2020-07-12
 */
@Data
@NoArgsConstructor
@ApiModel
public abstract class AbstractDateRangePageParam<T> extends AbstractPageParam<T> {
    @NotNull
    @ApiModelProperty(value = "开始日期，yyyy-MM-dd", required = true, position = 1)
    protected LocalDate startDate;

    @NotNull
    @ApiModelProperty(value = "结束日期，yyyy-MM-dd", required = true, position = 2)
    protected LocalDate endDate;
}
