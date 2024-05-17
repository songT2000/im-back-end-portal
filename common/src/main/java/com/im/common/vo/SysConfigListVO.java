package com.im.common.vo;

import com.im.common.entity.SysConfig;
import com.im.common.entity.enums.SysConfigGroupEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.cglib.beans.BeanCopier;

/**
 * 系统配置
 *
 * @author Barry
 * @date 2019-11-12
 */
@Data
@ApiModel
public class SysConfigListVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(SysConfig.class, SysConfigListVO.class, false);

    public SysConfigListVO(SysConfig e) {
        BEAN_COPIER.copy(e, this, null);
    }

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("配置组")
    private SysConfigGroupEnum group;

    @ApiModelProperty("配置项")
    private String item;

    @ApiModelProperty("配置值")
    private String value;

    @ApiModelProperty("是否是高级配置")
    private Boolean advance;

    @ApiModelProperty("是否开放编辑")
    private Boolean editable;

    @ApiModelProperty("配置项名称")
    private String name;

    @ApiModelProperty("备注")
    private String remark;
}
