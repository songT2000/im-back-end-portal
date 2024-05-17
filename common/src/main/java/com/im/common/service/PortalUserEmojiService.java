package com.im.common.service;

import com.im.common.entity.PortalUserEmoji;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.service.MyBatisPlusService;

public interface PortalUserEmojiService extends MyBatisPlusService<PortalUserEmoji> {

    /**
     * 删除表情包
     */
    RestResponse delete(Long id);
    /**
     * 新增表情包
     */
    RestResponse add(PortalUserEmoji param);

}
