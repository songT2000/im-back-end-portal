package com.im.common.param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.im.common.entity.PortalUser;
import com.im.common.entity.enums.PortalUserOrderByTypeEnum;
import com.im.common.util.mybatis.page.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 前台用户列表查询
 *
 * @author Barry
 * @date 2021-01-16
 */
@Data
@NoArgsConstructor
@ApiModel
public class PortalUserPageAdminParam extends AbstractPageParam<PortalUser> {
    @ApiModelProperty(value = "用户名", position = 1)
    private String username;

    @ApiModelProperty(value = "手机", position = 2)
    private String mobile;

    @ApiModelProperty(value = "昵称", position = 3)
    private String nickname;

    @ApiModelProperty(value = "用户组ID列表", position = 4)
    private Set<Long> userGroupIds;

    @ApiModelProperty(value = "提现姓名", position = 5)
    private String withdrawName;

    @ApiModelProperty(value = "最小余额", position = 6)
    private BigDecimal minBalance;

    @ApiModelProperty(value = "最大余额", position = 7)
    private BigDecimal maxBalance;

    @ApiModelProperty(value = "登录权限", position = 8)
    private Boolean loginEnabled;

    @ApiModelProperty(value = "充值权限", position = 9)
    private Boolean rechargeEnabled;

    @ApiModelProperty(value = "提现权限", position = 10)
    private Boolean withdrawEnabled;

    @ApiModelProperty(value = "加人权限", position = 11)
    private Boolean addFriendEnabled;

    @ApiModelProperty(value = "开始注册时间", position = 12)
    private LocalDateTime startCreateTime;

    @ApiModelProperty(value = "结束注册时间", position = 13)
    private LocalDateTime endCreateTime;

    @ApiModelProperty(value = "排序类型", required = true, position = 14)
    private PortalUserOrderByTypeEnum orderType;

    @ApiModelProperty(value = "邀请码", position = 15)
    private String inviteCode;

    @Override
    public Wrapper<PortalUser> toQueryWrapper(Object wrapperParam) {
        // mapper.xml里实现
        return null;
    }
}
