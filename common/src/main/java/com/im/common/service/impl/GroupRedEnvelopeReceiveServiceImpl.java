package com.im.common.service.impl;

import com.im.common.entity.GroupRedEnvelopeReceive;
import com.im.common.mapper.GroupRedEnvelopeReceiveMapper;
import com.im.common.service.GroupRedEnvelopeReceiveService;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 群红包领取记录 服务实现类
 *
 * @author Barry
 * @date 2021-12-20
 */
@Service
public class GroupRedEnvelopeReceiveServiceImpl
        extends MyBatisPlusServiceImpl<GroupRedEnvelopeReceiveMapper, GroupRedEnvelopeReceive>
        implements GroupRedEnvelopeReceiveService {
}
