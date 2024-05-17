package com.im.common.vo;

import com.im.common.entity.SysCircuit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

/**
 * 系统测速线路
 *
 * @author max.stark
 */
@Data
@NoArgsConstructor
@ApiModel
public class SysCircuitPortalVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(SysCircuit.class, SysCircuitPortalVO.class, false);

    public SysCircuitPortalVO(SysCircuit e) {
        BEAN_COPIER.copy(e, this, null);
    }

    @ApiModelProperty(value = "ID", position = 1)
    private Long id;

    @ApiModelProperty(value = "线路地址", position = 2)
    private String circuitUrl;

    @ApiModelProperty(value = "线路类型 1 web 2 app", position = 3)
    private Integer circuitType;

    @ApiModelProperty(value = "备注", position = 4)
    private String remark;
}
