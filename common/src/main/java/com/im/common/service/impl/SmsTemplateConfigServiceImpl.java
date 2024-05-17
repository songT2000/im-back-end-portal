package com.im.common.service.impl;

import com.im.common.entity.SmsTemplateConfig;
import com.im.common.mapper.SmsTemplateConfigMapper;
import com.im.common.service.SmsTemplateConfigService;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 短信模板配置 服务实现类
 *
 * @author Barry
 * @date 2022-02-10
 */
@Service
public class SmsTemplateConfigServiceImpl
        extends MyBatisPlusServiceImpl<SmsTemplateConfigMapper, SmsTemplateConfig>
        implements SmsTemplateConfigService {
}
