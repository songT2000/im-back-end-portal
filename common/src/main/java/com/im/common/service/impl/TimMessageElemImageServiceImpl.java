package com.im.common.service.impl;

import com.im.common.entity.tim.TimMessageElemImage;
import com.im.common.mapper.TimMessageElemImageMapper;
import com.im.common.service.TimMessageElemImageService;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class TimMessageElemImageServiceImpl
        extends MyBatisPlusServiceImpl<TimMessageElemImageMapper, TimMessageElemImage>
        implements TimMessageElemImageService {
}
