package com.im.common.service;

import com.im.common.entity.UserBankCardBlack;
import com.im.common.param.IdParam;
import com.im.common.param.UserBankCardBlackAddAdminParam;
import com.im.common.param.UserBankCardBlackEditAdminParam;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.service.MyBatisPlusService;
import com.im.common.vo.AdminSessionUser;

/**
 * 用户卡黑名单 服务类
 *
 * @author Barry
 * @date 2018/6/8
 */
public interface UserBankCardBlackService extends MyBatisPlusService<UserBankCardBlack> {
    /**
     * 新增，给管理后台使用的
     *
     * @param sessionUser
     * @param param 参数
     * @return OK
     */
    RestResponse addForAdmin(AdminSessionUser sessionUser, UserBankCardBlackAddAdminParam param);

    /**
     * 编辑，给管理后台使用的
     *
     * @param sessionUser
     * @param param 参数
     * @return OK
     */
    RestResponse editForAdmin(AdminSessionUser sessionUser, UserBankCardBlackEditAdminParam param);

    /**
     * 删除，给管理后台使用的
     *
     * @param param 参数
     * @return
     */
    RestResponse deleteForAdmin(IdParam param);
}
