package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 新增表情包专辑参数
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimFaceAddParam {

    /**
     * 专辑名称
     */
    @NotBlank
    @ApiModelProperty(value = "专辑名称", required = true)
    private String faceName;

    @NotBlank
    @ApiModelProperty(value = "聊天面板图标", required = true,position = 1)
    private String chatPanelIcon;

    @NotNull
    @ApiModelProperty(value = "表情地址集合", required = true,position = 2)
    private List<String> faceUrlList;

}
