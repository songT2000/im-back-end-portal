package com.im.common.param;

import com.im.common.entity.enums.PortalTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Barry
 * @date 2020-05-26
 */
@Data
@NoArgsConstructor
@ApiModel
public class UsernamePortalTypeParam {
    @NotBlank
    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    @NotNull
    @ApiModelProperty(value = "门户类型", required = true)
    private PortalTypeEnum portalType;
}
