package com.im.common.vo;

import com.im.common.entity.tim.TimGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

/**
 * 群组信息
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimGroupSimpleAdminVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(TimGroup.class, TimGroupSimpleAdminVO.class, false);

    public TimGroupSimpleAdminVO(TimGroup e) {
        BEAN_COPIER.copy(e, this, null);
    }

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("群组的ID")
    private String groupId;

    @ApiModelProperty("群名称")
    private String groupName;
}
