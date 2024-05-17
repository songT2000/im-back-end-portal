package com.im.common.service;


import com.im.common.entity.PortalAreaBlackWhite;
import com.im.common.param.IdParam;
import com.im.common.param.PortalAreaBlackWhiteAddAdminParam;
import com.im.common.param.PortalAreaBlackWhiteEditAdminParam;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.service.MyBatisPlusService;
import com.im.common.vo.AdminSessionUser;

/**
 * 前台区域黑白名单 服务类
 *
 * @author Max
 * @date 2021-02-27
 */
public interface PortalAreaBlackWhiteService extends MyBatisPlusService<PortalAreaBlackWhite> {
    /**
     * 添加，给管理后台使用的
     *
     * @param sessionUser
     * @param param       参数
     * @return OK
     */
    RestResponse addForAdmin(AdminSessionUser sessionUser, PortalAreaBlackWhiteAddAdminParam param);

    /**
     * 编辑，给管理后台使用的
     *
     * @param sessionUser
     * @param param       参数
     * @return OK
     */
    RestResponse editForAdmin(AdminSessionUser sessionUser, PortalAreaBlackWhiteEditAdminParam param);

    /**
     * 删除
     *
     * @param param 参数
     * @return
     */
    RestResponse deleteForAdmin(IdParam param);
}
