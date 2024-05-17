package com.im.common.param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.UserBankCardBlack;
import com.im.common.entity.UserBankCardBlack;
import com.im.common.util.StrUtil;
import com.im.common.util.mybatis.page.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户卡黑名单列表参数
 *
 * @author Barry
 * @date 2020-05-26
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserBankCardBlackPageAdminParam extends AbstractPageParam<UserBankCardBlack> {
    @ApiModelProperty(value = "持卡人姓名")
    private String cardName;

    @ApiModelProperty(value = "卡号")
    private String cardNum;

    @Override
    public Wrapper<UserBankCardBlack> toQueryWrapper(Object wrapperParam) {
        LambdaQueryWrapper<UserBankCardBlack> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(StrUtil.isNotBlank(cardName), UserBankCardBlack::getCardName, cardName);
        wrapper.eq(StrUtil.isNotBlank(cardNum), UserBankCardBlack::getCardNum, cardNum);

        wrapper.orderByDesc(UserBankCardBlack::getUpdateTime, UserBankCardBlack::getId);

        return wrapper;
    }
}
