package com.im.common.param;

import com.im.common.entity.enums.DeviceTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author Barry
 * @date 2020-05-26
 */
@Data
@NoArgsConstructor
@ApiModel
public class IdDeviceTypeParam {
    @NotNull
    @ApiModelProperty(value = "ID", required = true, position = 1)
    private Long id;

    @NotNull
    @ApiModelProperty(value = "设备类型", required = true, position = 2)
    private DeviceTypeEnum deviceType;
}
