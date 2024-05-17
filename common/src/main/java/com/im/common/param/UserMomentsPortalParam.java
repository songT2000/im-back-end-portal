package com.im.common.param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.im.common.entity.UserBill;
import com.im.common.entity.UserMoments;
import com.im.common.util.mybatis.page.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 前台朋友圈
 *
 * @author max.stark
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserMomentsPortalParam extends AbstractPageParam<UserMoments> {

    /**
     * 获取mybatis查询wrapper，子类实现该方法，返回null则不查询，返回其它则查询
     *
     * @param wrapperParam 封装wrapper时需要的参数
     * @return 返回null则不查询，返回其它则查询
     */
    @Override
    public Wrapper<UserMoments> toQueryWrapper(Object wrapperParam) {
        return null;
    }
}
