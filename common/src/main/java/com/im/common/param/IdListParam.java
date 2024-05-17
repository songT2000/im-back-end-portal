package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@ApiModel
public class IdListParam {

    @NotNull
    @ApiModelProperty(value = "ID集合", required = true)
    private List<Long> ids;
}
