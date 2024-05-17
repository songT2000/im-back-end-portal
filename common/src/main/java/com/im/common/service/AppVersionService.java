package com.im.common.service;

import com.im.common.entity.AppVersion;
import com.im.common.entity.enums.AppTypeEnum;
import com.im.common.param.AppVersionAddParam;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.service.MyBatisPlusService;
import com.im.common.vo.AppVersionVO;

public interface AppVersionService extends MyBatisPlusService<AppVersion> {

    /**
     * 查询最新版本
     * @param appType       应用类型
     */
    RestResponse<AppVersionVO> queryLatest(AppTypeEnum appType);

    /**
     * 新增
     */
    RestResponse add(AppVersionAddParam param);

    /**
     * 删除
     */
    RestResponse delete(Long id);
}
