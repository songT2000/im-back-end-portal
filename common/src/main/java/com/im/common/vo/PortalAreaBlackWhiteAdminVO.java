package com.im.common.vo;

import com.im.common.entity.PortalAreaBlackWhite;
import com.im.common.entity.enums.BlackWhiteTypeEnum;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDateTime;

/**
 * 前台区域黑白名单管理后台VO
 *
 * @author Max
 * @date 2021-02-27
 */
@Data
@NoArgsConstructor
@ApiModel
public class PortalAreaBlackWhiteAdminVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(PortalAreaBlackWhite.class, PortalAreaBlackWhiteAdminVO.class, false);

    public PortalAreaBlackWhiteAdminVO(PortalAreaBlackWhite e) {
        BEAN_COPIER.copy(e, this, null);
        this.createAdminUsername = UserUtil.getUsernameByIdFromLocal(e.getCreateAdminId(), PortalTypeEnum.ADMIN);
        if (e.getUpdateAdminId() != null) {
            this.updateAdminUsername = UserUtil.getUsernameByIdFromLocal(e.getUpdateAdminId(), PortalTypeEnum.ADMIN);
        } else {
            this.updateTime = null;
        }
    }

    @ApiModelProperty(value = "ID", position = 1)
    private Long id;

    @ApiModelProperty(value = "区域，模糊匹配，只要区域中包含字样就成立", position = 2)
    private String area;

    @ApiModelProperty(value = "黑白名单", position = 3)
    private BlackWhiteTypeEnum blackWhite;

    @ApiModelProperty(value = "备注", position = 4)
    private String remark;

    @ApiModelProperty(value = "创建人", position = 5)
    private String createAdminUsername;

    @ApiModelProperty(value = "创建时间", position = 6)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改人", position = 7)
    private String updateAdminUsername;

    @ApiModelProperty(value = "修改时间", position = 8)
    private LocalDateTime updateTime;
}
