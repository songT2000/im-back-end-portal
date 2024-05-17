package com.im.common.entity;

import com.im.common.entity.base.BaseEntity;
import com.im.common.entity.enums.BlackWhiteTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 后台IP黑白名单
 *
 * @author max.stark
 */
@Data
@NoArgsConstructor
public class AdminIpBlackWhite extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 2040183512600686274L;

    /**
     * IP，全文匹配，支持IPV4/IPV4掩码/IPV4段/IPV6格式
     **/
    private String ip;

    /**
     * 黑白名单
     */
    private BlackWhiteTypeEnum blackWhite;

    /**
     * 修改人
     **/
    private Long updateAdminId;

    /**
     * 备注
     */
    private String remark;
}
