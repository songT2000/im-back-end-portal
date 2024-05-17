package com.im.common.param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.UserBankCard;
import com.im.common.entity.UserBankCard;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.util.StrUtil;
import com.im.common.util.mybatis.page.AbstractPageParam;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用银行卡分页参数
 *
 * @author max.stark
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserBankCardPageAdminParam extends AbstractPageParam<UserBankCard> {
    @ApiModelProperty(value = "用户名", position = 1)
    private String username;

    @ApiModelProperty(value = "银行ID", position = 2)
    private Long bankId;

    @ApiModelProperty(value = "卡号", position = 3)
    private String cardNum;

    @ApiModelProperty(value = "是否启用", position = 4)
    private Boolean enabled;

    @Override
    public Wrapper<UserBankCard> toQueryWrapper(Object wrapperParam) {
        LambdaQueryWrapper<UserBankCard> wrapper = new LambdaQueryWrapper<>();

        if (StrUtil.isNotBlank(username)) {
            Long id = UserUtil.getUserIdByUsernameFromLocal(username, PortalTypeEnum.PORTAL);
            if (id == null) {
                // 没有查询到用户，不再查询
                return null;
            }
            wrapper.eq(UserBankCard::getUserId, id);
        }

        wrapper.eq(StrUtil.isNotBlank(cardNum), UserBankCard::getCardNum, cardNum);
        wrapper.eq(bankId != null, UserBankCard::getBankId, bankId);
        wrapper.eq(enabled != null, UserBankCard::getEnabled, enabled);

        wrapper.orderByDesc(UserBankCard::getId);

        return wrapper;
    }
}
