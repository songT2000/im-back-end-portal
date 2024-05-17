package com.im.common.param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.UserBalanceSnapshot;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.util.DateTimeUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.mybatis.page.AbstractPageParam;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author Barry
 * @date 2020-05-26
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserBalanceSnapshotPageParam extends AbstractPageParam<UserBalanceSnapshot> {

    @ApiModelProperty(value = "用户名", required = true, position = 1)
    private String username;

    @ApiModelProperty(value = "币ID", required = true, position = 2)
    private Long vcId;

    @ApiModelProperty(value = "开始日期，yyyy-MM-dd，可以为空", position = 3)
    private LocalDate startDate;

    @ApiModelProperty(value = "结束日期，yyyy-MM-dd，可以为空", position = 4)
    private LocalDate endDate;

    /**
     * 获取mybatis查询wrapper，子类实现该方法，返回null则不查询，返回其它则查询
     *
     * @param wrapperParam 封装wrapper时需要的参数
     * @return 返回null则不查询，返回其它则查询
     */
    @Override
    public Wrapper<UserBalanceSnapshot> toQueryWrapper(Object wrapperParam) {
        LambdaQueryWrapper<UserBalanceSnapshot> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(username)) {
            Long userId = UserUtil.getUserIdByUsernameFromLocal(username, PortalTypeEnum.PORTAL);
            wrapper.eq(UserBalanceSnapshot::getUserId, userId);
        }
        wrapper.ge(startDate != null, UserBalanceSnapshot::getDate, DateTimeUtil.toDateStr(startDate));
        wrapper.le(endDate != null, UserBalanceSnapshot::getDate, DateTimeUtil.toDateStr(endDate));
        wrapper.orderByDesc(UserBalanceSnapshot::getDate);
        wrapper.orderByAsc(UserBalanceSnapshot::getUserId);
        return wrapper;
    }
}
