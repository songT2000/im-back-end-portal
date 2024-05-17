package com.im.common.service;

import com.im.common.entity.PortalNavigator;
import com.im.common.param.IdEnableDisableParam;
import com.im.common.param.IdParam;
import com.im.common.param.PortalNavigatorAddAdminParam;
import com.im.common.param.PortalNavigatorEditAdminParam;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.service.MyBatisPlusService;

/**
 * 前台导航 服务类
 *
 * @author Barry
 * @date 2022-03-25
 */
public interface PortalNavigatorService extends MyBatisPlusService<PortalNavigator> {
    /**
     * 新增
     *
     * @param param
     * @return
     */
    RestResponse addForAdmin(PortalNavigatorAddAdminParam param);

    /**
     * 编辑
     *
     * @param param
     * @return
     */
    RestResponse editForAdmin(PortalNavigatorEditAdminParam param);

    /**
     * 启/禁
     *
     * @param param
     * @return
     */
    RestResponse enableDisableForAdmin(IdEnableDisableParam param);

    /**
     * 删除
     *
     * @param param
     * @return
     */
    RestResponse deleteForAdmin(IdParam param);
}
