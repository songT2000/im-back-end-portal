package com.im.common.entity;

import com.im.common.entity.base.BaseEntity;
import com.im.common.entity.enums.BlackWhiteTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 前台区域黑白名单
 *
 * @author Max
 * @date 2021-02-27
 */
@Data
@NoArgsConstructor
public class PortalAreaBlackWhite extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -6855033715184064074L;

    /**
     * 区域，模糊匹配，只要区域中包含字样就成立
     **/
    private String area;

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
