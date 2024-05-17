package com.im.common.service;

import com.im.common.entity.SysCircuit;
import com.im.common.param.IdEnableDisableParam;
import com.im.common.param.IdParam;
import com.im.common.param.SysCircuitAddAdminParam;
import com.im.common.param.SysCircuitEditAdminParam;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.service.MyBatisPlusService;


/**
 * 系统公告 接口
 *
 * @author max.stark
 */
public interface SysCircuitService extends MyBatisPlusService<SysCircuit> {
    /**
     * 新增
     *
     * @param param
     * @return
     */
    RestResponse addForAdmin(SysCircuitAddAdminParam param);

    /**
     * 编辑
     *
     * @param param
     * @return
     */
    RestResponse editForAdmin(SysCircuitEditAdminParam param);

    /**
     * 删除
     *
     * @param param
     * @return
     */
    RestResponse deleteForAdmin(IdParam param);

    /**
     * 删除
     *
     * @param param
     * @return 返回OK表示成功
     */
    RestResponse enableDisableForAdmin(IdEnableDisableParam param);
}
