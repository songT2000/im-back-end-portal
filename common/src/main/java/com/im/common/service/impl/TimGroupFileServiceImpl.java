package com.im.common.service.impl;

import com.im.common.entity.tim.TimGroupFile;
import com.im.common.mapper.TimGroupFileMapper;
import com.im.common.service.TimGroupFileService;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class TimGroupFileServiceImpl extends MyBatisPlusServiceImpl<TimGroupFileMapper, TimGroupFile> implements TimGroupFileService {

}
