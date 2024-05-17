package com.im.common.service.impl;

import com.im.common.entity.tim.TimMessageElemLocation;
import com.im.common.mapper.TimMessageElemLocationMapper;
import com.im.common.service.TimMessageElemLocationService;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class TimMessageElemLocationServiceImpl
        extends MyBatisPlusServiceImpl<TimMessageElemLocationMapper, TimMessageElemLocation>
        implements TimMessageElemLocationService {
}
