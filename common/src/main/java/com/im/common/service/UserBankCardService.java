package com.im.common.service;

import com.im.common.entity.UserBankCard;
import com.im.common.param.*;
import com.im.common.param.PortalUserBankCardAddParam;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.service.MyBatisPlusService;
import com.im.common.vo.PortalSessionUser;
import com.im.common.vo.UserBankCardPortalVO;

import java.util.List;

/**
 * 用户银行卡 服务类
 *
 * @author Max
 * @date 2020/6/8
 */
public interface UserBankCardService extends MyBatisPlusService<UserBankCard> {
    /**
     * 根据用户ID和银行卡ID来获取
     *
     * @param userId
     * @param cardId
     * @return
     */
    UserBankCard getByUserIdAndCardId(long userId, long cardId);

    /**
     * 列表，给前台用的
     *
     * @param sessionUser 当前登录用户
     * @return
     */
    List<UserBankCardPortalVO> listVOForPortal(PortalSessionUser sessionUser);

    /**
     * 添加，给前台使用的
     *
     * @param sessionUser 当前登录用户
     * @param param       参数
     * @return OK
     */
    RestResponse addForPortal(PortalSessionUser sessionUser, PortalUserBankCardAddParam param);

    /**
     * 删除
     *
     * @param param 参数
     * @return
     */
    RestResponse deleteForPortal(PortalSessionUser sessionUser, IdParam param);

    /**
     * 添加用户银行卡，后台使用
     * @param param
     * @return
     */
    RestResponse addForAdmin(UserBankCardAddAdminParam param);

    /**
     * 启用禁用 银行卡  后台使用
     * @param param
     * @return
     */
    RestResponse enableDisableForAdmin(IdEnableDisableParam param);

    /**
     * 删除 后台使用
     * @param param
     * @return
     */
    RestResponse deleteForAdmin(IdParam param);

    /**
     * 统计用户绑定了多少张卡，仅启用
     *
     * @param userId
     */
    int countUserEnabledBankCard(long userId);
}
