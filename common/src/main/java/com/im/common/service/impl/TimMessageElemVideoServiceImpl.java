package com.im.common.service.impl;

import com.im.common.entity.tim.TimMessageElemVideo;
import com.im.common.mapper.TimMessageElemVideoMapper;
import com.im.common.service.TimMessageElemVideoService;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class TimMessageElemVideoServiceImpl
        extends MyBatisPlusServiceImpl<TimMessageElemVideoMapper, TimMessageElemVideo>
        implements TimMessageElemVideoService {
}
