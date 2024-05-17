package com.im.common.service.impl;

import com.im.common.entity.tim.TimMessageElemCustom;
import com.im.common.mapper.TimMessageElemCustomMapper;
import com.im.common.service.TimMessageElemCustomService;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class TimMessageElemCustomServiceImpl
        extends MyBatisPlusServiceImpl<TimMessageElemCustomMapper, TimMessageElemCustom>
        implements TimMessageElemCustomService {
}
