package com.im.common.service;

import com.im.common.entity.AdminIpBlackWhite;
import com.im.common.param.AdminIpBlackWhiteAddAdminParam;
import com.im.common.param.AdminIpBlackWhiteEditAdminParam;
import com.im.common.param.IdGoogleParam;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.service.MyBatisPlusService;
import com.im.common.vo.AdminSessionUser;

/**
 * 后台黑白名单
 *
 * @author Barry
 * @date 2021-06-24
 */
public interface AdminIpBlackWhiteService extends MyBatisPlusService<AdminIpBlackWhite> {
    /**
     * 新增
     *
     * @param sessionUser
     * @param param
     * @return
     */
    RestResponse addForAdmin(AdminSessionUser sessionUser, AdminIpBlackWhiteAddAdminParam param);

    /**
     * 编辑
     *
     * @param sessionUser
     * @param param
     * @return
     */
    RestResponse editForAdmin(AdminSessionUser sessionUser, AdminIpBlackWhiteEditAdminParam param);

    /**
     * 删除
     *
     * @param sessionUser
     * @param param
     * @return
     */
    RestResponse deleteForAdmin(AdminSessionUser sessionUser, IdGoogleParam param);
}
