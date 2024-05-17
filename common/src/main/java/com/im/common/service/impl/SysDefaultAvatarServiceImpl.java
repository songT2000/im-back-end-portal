package com.im.common.service.impl;

import com.im.common.entity.SysDefaultAvatar;
import com.im.common.mapper.SysDefaultAvatarMapper;
import com.im.common.param.SysDefaultAvatarAddParam;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.SysDefaultAvatarService;
import com.im.common.util.NumberUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统默认头像 服务实现类
 *
 * @author Barry
 * @date 2022-04-07
 */
@Service
public class SysDefaultAvatarServiceImpl
        extends MyBatisPlusServiceImpl<SysDefaultAvatarMapper, SysDefaultAvatar>
        implements SysDefaultAvatarService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse addForAdmin(SysDefaultAvatarAddParam param) {
        String url = StrUtil.trim(param.getUrl());

        // 是否重复
        Integer count = lambdaQuery().eq(SysDefaultAvatar::getUrl, url).count();
        if (NumberUtil.isGreatThenZero(count)) {
            return RestResponse.failed(ResponseCode.SYS_DATA_EXISTED);
        }

        SysDefaultAvatar avatar = new SysDefaultAvatar();
        avatar.setUrl(url);

        boolean saved = save(avatar);

        return saved ? RestResponse.OK : RestResponse.SYS_DATA_STATUS_ERROR;
    }
}
