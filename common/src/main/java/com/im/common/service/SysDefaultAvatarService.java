package com.im.common.service;

import com.im.common.entity.SysDefaultAvatar;
import com.im.common.param.SysDefaultAvatarAddParam;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.service.MyBatisPlusService;

/**
 * 系统默认头像 服务类
 *
 * @author Barry
 * @date 2022-04-07
 */
public interface SysDefaultAvatarService extends MyBatisPlusService<SysDefaultAvatar> {
    /**
     * 新增
     *
     * @param param 参数
     * @return
     */
    RestResponse addForAdmin(SysDefaultAvatarAddParam param);
}
