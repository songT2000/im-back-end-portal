package com.im.common.service.impl;

import com.im.common.entity.tim.TimMessageElemText;
import com.im.common.mapper.TimMessageElemTextMapper;
import com.im.common.service.TimMessageElemTextService;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class TimMessageElemTextServiceImpl
        extends MyBatisPlusServiceImpl<TimMessageElemTextMapper, TimMessageElemText>
        implements TimMessageElemTextService {
}
