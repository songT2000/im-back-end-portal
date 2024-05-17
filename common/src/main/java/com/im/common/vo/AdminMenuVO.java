package com.im.common.vo;

import com.im.common.entity.AdminMenu;
import com.im.common.entity.enums.AdminMenuTypeEnum;
import com.im.common.util.mybatis.typehandler.i18n.I18nString;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.cglib.beans.BeanCopier;

/**
 * 菜单
 *
 * @author Barry
 * @date 2019-11-12
 */
@Data
@ApiModel
public class AdminMenuVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(AdminMenu.class, AdminMenuVO.class, false);

    public AdminMenuVO(AdminMenu menu) {
        BEAN_COPIER.copy(menu, this, null);
    }

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("权限编码")
    private String code;

    @ApiModelProperty("菜单类型")
    private AdminMenuTypeEnum type;

    @ApiModelProperty("上级菜单ID，0则没有")
    private Long parentId;

    @ApiModelProperty("名称")
    private I18nString name;

    @ApiModelProperty("菜单链接")
    private String url;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("排序号")
    private Integer sort;

    @ApiModelProperty("是否启用")
    private Boolean enabled;
}
