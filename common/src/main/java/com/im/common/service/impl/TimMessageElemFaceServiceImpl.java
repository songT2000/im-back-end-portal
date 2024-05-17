package com.im.common.service.impl;

import com.im.common.entity.tim.TimMessageElemFace;
import com.im.common.mapper.TimMessageElemFaceMapper;
import com.im.common.service.TimMessageElemFaceService;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class TimMessageElemFaceServiceImpl
        extends MyBatisPlusServiceImpl<TimMessageElemFaceMapper, TimMessageElemFace>
        implements TimMessageElemFaceService {
}
