package com.im.common.param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.UserBill;
import com.im.common.entity.UserMoments;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.enums.TrendsTypeEnum;
import com.im.common.exception.ImException;
import com.im.common.response.ResponseCode;
import com.im.common.util.CollectionUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.mybatis.page.AbstractPageParam;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


/**
 * 朋友圈分页
 * @author max.stark
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserMomentsPageAdminParam extends AbstractPageParam<UserMoments> {
    @ApiModelProperty(value = "用户名", position = 1)
    private String username;

    @ApiModelProperty(value = "内容", position = 2)
    private String content;

    @ApiModelProperty(value = "类型", position = 3)
    private TrendsTypeEnum trendsType;

    @NotNull
    @ApiModelProperty(value = "发布开始时间，yyyy-MM-dd HH:mm:ss", required = true, position =4)
    private LocalDateTime startCreateTime;

    @NotNull
    @ApiModelProperty(value = "发布结束时间，yyyy-MM-dd HH:mm:ss", required = true, position = 5)
    private LocalDateTime endCreateTime;

    @Override
    public Wrapper<UserMoments> toQueryWrapper(Object wrapperParam) {
        LambdaQueryWrapper<UserMoments> wrapper = new LambdaQueryWrapper<>();

        if (StrUtil.isNotBlank(username)) {
            Long id = UserUtil.getUserIdByUsernameFromLocal(username, PortalTypeEnum.PORTAL);
            if (id == null) {
                // 没有查询到用户，不再查询
                return null;
            }

            wrapper.eq(UserMoments::getUserId, id);
        }

        wrapper.like(StrUtil.isNotBlank(content), UserMoments::getContent, content);
        wrapper.eq(trendsType != null, UserMoments::getTrendsType, trendsType);

        wrapper.ge(UserMoments::getCreateTime, startCreateTime);
        wrapper.le(UserMoments::getCreateTime, endCreateTime);

        wrapper.orderByDesc(UserMoments::getId);

        return wrapper;
    }
}
