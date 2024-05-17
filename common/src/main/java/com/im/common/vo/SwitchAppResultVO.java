package com.im.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 迁移过程中导入数据的进度缓存
 */
@Data
@NoArgsConstructor
@ApiModel
public class SwitchAppResultVO {

    @ApiModelProperty("总记录数")
    private Integer total;

    @ApiModelProperty("当前处理数")
    private Integer current;

    @ApiModelProperty("是否失败")
    private Boolean fail;

    @ApiModelProperty("错误信息")
    private String message;

}
