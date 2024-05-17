package com.im.common.service.impl;

import com.im.common.entity.SmsChannelConfig;
import com.im.common.mapper.SmsChannelConfigMapper;
import com.im.common.service.SmsChannelConfigService;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 短信信令通道配置 服务实现类
 *
 * @author Barry
 * @date 2022-02-10
 */
@Service
public class SmsChannelConfigServiceImpl
        extends MyBatisPlusServiceImpl<SmsChannelConfigMapper, SmsChannelConfig>
        implements SmsChannelConfigService {
}
