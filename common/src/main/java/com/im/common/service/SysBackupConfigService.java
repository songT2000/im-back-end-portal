package com.im.common.service;

import com.im.common.entity.SysBackupConfig;
import com.im.common.param.IdEnableDisableGoogleParam;
import com.im.common.param.IdGoogleParam;
import com.im.common.param.SysBackupConfigEditParam;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.service.MyBatisPlusService;
import com.im.common.vo.AdminSessionUser;

/**
 * 数据备份配置服务类 -> sys_backup_config
 *
 * @author Barry
 * @date 2020/01/04
 */
public interface SysBackupConfigService extends MyBatisPlusService<SysBackupConfig> {
    /**
     * 编辑数据
     *
     * @param sessionUser
     * @param param 参数
     * @return RestResponse
     */
    RestResponse editForAdmin(AdminSessionUser sessionUser, SysBackupConfigEditParam param);

    /**
     * 启用/禁用数据备份
     *
     * @param sessionUser
     * @param param 参数
     * @return RestResponse
     */
    RestResponse enableDisableForAdmin(AdminSessionUser sessionUser, IdEnableDisableGoogleParam param);

    /**
     * 删除数据备份
     *
     * @param sessionUser
     * @param param 参数
     * @return RestResponse
     */
    RestResponse deleteForAdmin(AdminSessionUser sessionUser, IdGoogleParam param);
}
