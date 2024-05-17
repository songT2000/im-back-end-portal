package com.im.common.service.impl;

import com.im.common.entity.tim.TimMessageElemSound;
import com.im.common.mapper.TimMessageElemSoundMapper;
import com.im.common.service.TimMessageElemSoundService;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class TimMessageElemSoundServiceImpl 
        extends MyBatisPlusServiceImpl<TimMessageElemSoundMapper, TimMessageElemSound> 
        implements TimMessageElemSoundService {
}
