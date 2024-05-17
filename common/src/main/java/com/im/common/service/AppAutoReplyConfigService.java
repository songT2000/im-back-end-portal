package com.im.common.service;

import com.im.common.entity.AppAutoReplyConfig;
import com.im.common.param.AppAutoReplyConfigAddParam;
import com.im.common.param.IdParam;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.service.MyBatisPlusService;

/**
 * 自动回复消息配置 服务类
 */
public interface AppAutoReplyConfigService extends MyBatisPlusService<AppAutoReplyConfig> {

    /**
     * 新增配置
     *
     * @param param 参数
     * @return 返回OK表示成功
     */
    RestResponse add(AppAutoReplyConfigAddParam param);

    /**
     * 删除配置
     *
     * @param param 参数
     * @return 返回OK表示成功
     */
    RestResponse delete(IdParam param);

}
