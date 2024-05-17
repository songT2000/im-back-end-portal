package com.im.common.param;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.UserBill;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.enums.UserBillTypeEnum;
import com.im.common.entity.tim.TimGroup;
import com.im.common.exception.ImException;
import com.im.common.response.ResponseCode;
import com.im.common.util.CollectionUtil;
import com.im.common.util.mybatis.page.AbstractPageParam;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 查询群组信息分页参数
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimGroupPageParam extends AbstractPageParam<TimGroup> {
    @ApiModelProperty(value = "群名称", position = 1)
    private String groupName;

    @ApiModelProperty(value = "群主账号", position = 2)
    private String ownerUsername;

    @Override
    public Wrapper<TimGroup> toQueryWrapper(Object wrapperParam) {
        LambdaQueryWrapper<TimGroup> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(ownerUsername)) {
            Long id = UserUtil.getUserIdByUsernameFromLocal(ownerUsername, PortalTypeEnum.PORTAL);
            if (id == null) {
                // 没有查询到用户，不再查询
                return null;
            }

            wrapper.eq(TimGroup::getOwnerUserId, id);
        }
        wrapper.like(StrUtil.isNotBlank(groupName), TimGroup::getGroupName, groupName);

        wrapper.orderByDesc(CollectionUtil.newArrayList(TimGroup::getCreateTime, TimGroup::getId));

        return wrapper;
    }
}
