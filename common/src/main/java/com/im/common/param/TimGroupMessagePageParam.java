package com.im.common.param;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.tim.TimGroup;
import com.im.common.entity.tim.TimGroupMember;
import com.im.common.entity.tim.TimMessageGroup;
import com.im.common.service.TimGroupService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.LocalDateTimeUtil;
import com.im.common.util.mybatis.page.AbstractDateRangePageParam;
import com.im.common.util.mybatis.page.AbstractPageParam;
import com.im.common.util.spring.SpringContextUtil;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 查询群组信息分页参数
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimGroupMessagePageParam extends AbstractDateRangePageParam<TimMessageGroup> {

    @ApiModelProperty(value = "群组Id")
    private String groupId;

    @ApiModelProperty(value = "群组名称")
    private String groupName;

    @ApiModelProperty(value = "发言人账号")
    private String username;


    @Override
    public Wrapper<TimMessageGroup> toQueryWrapper(Object wrapperParam) {
        LambdaQueryWrapper<TimMessageGroup> wrapper = new LambdaQueryWrapper<>();

        if (StrUtil.isNotBlank(username)) {
            Long id = UserUtil.getUserIdByUsernameFromLocal(username, PortalTypeEnum.PORTAL);
            if (id == null) {
                // 没有查询到用户，不再查询
                return null;
            }

            wrapper.eq(TimMessageGroup::getFromUserId, id);
        }
        if (StrUtil.isNotBlank(groupName)) {
            TimGroupService timGroupService = SpringContextUtil.getBean(TimGroupService.class);
            if(timGroupService==null){
                return null;
            }
            List<TimGroup> list = timGroupService.lambdaQuery().like(TimGroup::getGroupName, groupName).select(TimGroup::getGroupId).list();
            if (CollectionUtil.isEmpty(list)) {
                // 没有查询到对应的群组，不再查询
                return null;
            }

            wrapper.in(TimMessageGroup::getGroupId, list.stream().map(TimGroup::getGroupId).collect(Collectors.toList()));
        }

        wrapper.eq(StrUtil.isNotBlank(groupId),TimMessageGroup::getGroupId,groupId);
        wrapper.ge(startDate!=null,TimMessageGroup::getSendTime,startDate);
        wrapper.le(endDate!=null,TimMessageGroup::getSendTime, LocalDateTimeUtil.getLocalDateEndOfDay(endDate));

        wrapper.orderByDesc(CollectionUtil.newArrayList(TimMessageGroup::getSendTime));

        return wrapper;
    }
}
