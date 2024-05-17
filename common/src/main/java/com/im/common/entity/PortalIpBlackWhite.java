package com.im.common.entity;

import com.im.common.entity.base.BaseEntity;
import com.im.common.entity.enums.BlackWhiteTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 前台IP黑白名单
 *
 * @author Max
 * @date 2021-02-21
 */
@Data
@NoArgsConstructor
public class PortalIpBlackWhite extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -9045140684188570581L;

    /**
     * 用户名列表，多个用英文逗号分割，没有就是全局
     **/
    private String usernames;

    /**
     * IP，全文匹配，支持IPV4/IPV4掩码/IPV4段/IPV6格式，多个用英文逗号分割
     **/
    private String ip;

    /**
     * 黑白名单
     **/
    private BlackWhiteTypeEnum blackWhite;

    /**
     * 创建人
     **/
    private Long createAdminId;

    /**
     * 修改人
     **/
    private Long updateAdminId;

    /**
     * 备注
     */
    private String remark;
}
