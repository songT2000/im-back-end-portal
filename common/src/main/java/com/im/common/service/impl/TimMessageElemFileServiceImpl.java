package com.im.common.service.impl;

import com.im.common.entity.tim.TimMessageElemFile;
import com.im.common.mapper.TimMessageElemFileMapper;
import com.im.common.service.TimMessageElemFileService;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class TimMessageElemFileServiceImpl
        extends MyBatisPlusServiceImpl<TimMessageElemFileMapper, TimMessageElemFile>
        implements TimMessageElemFileService {
}
