package com.im.common.util.google.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 谷歌绑定VO
 *
 * @author Barry
 * @date 2018/5/13
 */
@Data
@ApiModel
public class GoogleBindVO {
    // 提供两种绑定方式，扫码二维码和手动输入验证码
    // 因为不排除用户手机无法扫码，或摄像头无法扫描等

    @ApiModelProperty(value = "扫码二维码，这是一张图片，base64编码，直接设置到image的src中即可显示", position = 1)
    private String qrCode;

    @ApiModelProperty(value = "或在手机上手动输入这个密钥", position = 2)
    private String key;

    public GoogleBindVO(String qrCode, String key) {
        this.qrCode = qrCode;
        this.key = key;
    }
}
