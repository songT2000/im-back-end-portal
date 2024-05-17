package com.im.common.param;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.tim.TimMessageC2c;
import com.im.common.entity.tim.TimMessageGroup;
import com.im.common.util.CollectionUtil;
import com.im.common.util.LocalDateTimeUtil;
import com.im.common.util.mybatis.page.AbstractDateRangePageParam;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询单聊消息分页参数
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimC2cMessagePageParam extends AbstractDateRangePageParam<TimMessageC2c> {

    @ApiModelProperty(value = "参与人1的账号")
    private String usernamePart1;

    @ApiModelProperty(value = "参与人2的账号")
    private String usernamePart2;


    @Override
    public Wrapper<TimMessageC2c> toQueryWrapper(Object wrapperParam) {
        LambdaQueryWrapper<TimMessageC2c> wrapper = new LambdaQueryWrapper<>();

        List<Long> userIds = new ArrayList<>();
        if (StrUtil.isNotBlank(usernamePart1)) {
            Long id = UserUtil.getUserIdByUsernameFromLocal(usernamePart1, PortalTypeEnum.PORTAL);
            if (id != null) {
                userIds.add(id);
            }else{
                // 没有查询到用户，不再查询
                return null;
            }
        }
        if (StrUtil.isNotBlank(usernamePart2)) {
            Long id = UserUtil.getUserIdByUsernameFromLocal(usernamePart2, PortalTypeEnum.PORTAL);
            if (id != null) {
                userIds.add(id);
            }else{
                // 没有查询到用户，不再查询
                return null;
            }
        }
        if(CollectionUtil.isNotEmpty(userIds)){
            if(userIds.size()==1){
                //如果只选择了一方，(fromUserId = 1 or toUserId = 1)
                wrapper.or(w->w.eq(TimMessageC2c::getFromUserId,userIds.get(0))
                        .or()
                        .eq(TimMessageC2c::getToUserId,userIds.get(0)));
            }else{
                //如果选择了双方，fromUserId in(1,2) and toUserId in (1,2)
                wrapper.in(TimMessageC2c::getFromUserId,userIds)
                        .in(TimMessageC2c::getToUserId,userIds);
            }
        }

        wrapper.ge(startDate!=null,TimMessageC2c::getSendTime,startDate);
        wrapper.le(endDate!=null,TimMessageC2c::getSendTime, LocalDateTimeUtil.getLocalDateEndOfDay(endDate));

        wrapper.orderByDesc(CollectionUtil.newArrayList(TimMessageC2c::getSendTime));

        return wrapper;
    }
}
