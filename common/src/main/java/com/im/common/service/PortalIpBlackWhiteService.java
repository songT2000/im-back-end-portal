package com.im.common.service;


import com.im.common.entity.PortalIpBlackWhite;
import com.im.common.param.IdParam;
import com.im.common.param.PortalIpBlackWhiteAddAdminParam;
import com.im.common.param.PortalIpBlackWhiteEditAdminParam;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.service.MyBatisPlusService;
import com.im.common.vo.AdminSessionUser;

/**
 * 前台IP黑白名单 服务类
 *
 * @author Max
 * @date 2021/02/21
 */
public interface PortalIpBlackWhiteService extends MyBatisPlusService<PortalIpBlackWhite> {
    /**
     * 添加，给管理后台使用的
     *
     * @param sessionUser
     * @param param 参数
     * @return OK
     */
    RestResponse addForAdmin(AdminSessionUser sessionUser, PortalIpBlackWhiteAddAdminParam param);

    /**
     * 编辑，给管理后台使用的
     *
     * @param sessionUser
     * @param param 参数
     * @return OK
     */
    RestResponse editForAdmin(AdminSessionUser sessionUser, PortalIpBlackWhiteEditAdminParam param);

    /**
     * 删除
     *
     * @param param 参数
     * @return
     */
    RestResponse deleteForAdmin(IdParam param);
}
