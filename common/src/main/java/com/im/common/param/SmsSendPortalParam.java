package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 前台发送短信验证码
 *
 * @author Barry
 * @date 2022-02-10
 */
@Data
@NoArgsConstructor
@ApiModel
public class SmsSendPortalParam {
    @NotBlank
    @ApiModelProperty(value = "国家编码，从[短信支持国家列表]获取", required = true, position = 1)
    private String countryCode;

    @NotBlank
    @ApiModelProperty(value = "手机，比如中国大陆13333333333，或者菲律宾9991112222", required = true, position = 2)
    private String mobile;
}
