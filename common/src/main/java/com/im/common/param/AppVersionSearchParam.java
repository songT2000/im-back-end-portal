package com.im.common.param;

import com.im.common.entity.enums.AppTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@ApiModel
public class AppVersionSearchParam {

    @ApiModelProperty(value = "应用类型", required = true)
    private AppTypeEnum appType;
}
