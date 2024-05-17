package com.im.common.vo;

import com.im.common.entity.SysCircuit;
import com.im.common.entity.enums.SysCircuitTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDateTime;

/**
 * 系统公告
 *
 * @author max.stark
 */
@Data
@NoArgsConstructor
@ApiModel
public class SysCircuitAdminVO {
    private static final BeanCopier BEAN_COPIER = BeanCopier.create(SysCircuit.class, SysCircuitAdminVO.class, false);

    public SysCircuitAdminVO(SysCircuit e) {
        BEAN_COPIER.copy(e, this, null);
    }

    @ApiModelProperty(value = "ID", position = 1)
    private Long id;

    @ApiModelProperty(value = "测速地址", position = 2)
    private String circuitUrl;

    @ApiModelProperty(value = "类型", position = 3)
    private SysCircuitTypeEnum circuitType;

    @ApiModelProperty(value = "第三方地址", position = 4)
    private String thirdPartyAddress;

    @ApiModelProperty(value = "是否启用", position = 5)
    private Boolean enabled;

    @ApiModelProperty(value = "备注", position = 6)
    private String remark;

    @ApiModelProperty(value = "创建时间", position = 7)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "最后修改时间", position = 8)
    private LocalDateTime updateTime;

}
