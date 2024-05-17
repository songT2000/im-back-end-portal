package com.im.common.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.entity.AdminRole;
import com.im.common.util.fastjson.I18nStringDeserializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDateTime;

/**
 * 系统角色VO
 *
 * @author Barry
 * @date 2019-11-15
 */
@Data
@NoArgsConstructor
@ApiModel
public class AdminRoleVO {
    private static final BeanCopier BEAN_COPIER = BeanCopier.create(AdminRole.class, AdminRoleVO.class, false);

    public AdminRoleVO(AdminRole adminRole) {
        BEAN_COPIER.copy(adminRole, this, null);
    }

    @ApiModelProperty("ID")
    private Long id;

    @JSONField(deserializeUsing = I18nStringDeserializer.class)
    @ApiModelProperty("角色名称")
    private String name;

    @ApiModelProperty("上级角色ID，0则没有，0表示拥有系统最高权限（自动拥有所有权限，不检查权限）")
    private Long parentId;

    @ApiModelProperty("排序号")
    private Integer sort;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("是否启用")
    private Boolean enabled;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
