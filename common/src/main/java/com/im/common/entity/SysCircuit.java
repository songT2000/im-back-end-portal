package com.im.common.entity;

import com.im.common.entity.base.BaseEnableEntity;
import com.im.common.entity.enums.SysCircuitTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 系统线路
 *
 * @author max.stark
 */
@Data
@NoArgsConstructor
public class SysCircuit extends BaseEnableEntity implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     *  线路地址
     */
    private String circuitUrl;

    /**
     * 类型
     */
    private SysCircuitTypeEnum circuitType;

    /**
     *  第三方地址
     */
    private String thirdPartyAddress;

    /**
     * 备注
     */
    private String remark;
}
