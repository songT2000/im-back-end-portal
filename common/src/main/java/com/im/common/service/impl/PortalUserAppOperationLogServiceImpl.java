package com.im.common.service.impl;

import com.im.common.entity.PortalUserAppOperationLog;
import com.im.common.mapper.PortalUserAppOperationLogMapper;
import com.im.common.service.PortalUserAppOperationLogService;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 用户app操作日志 服务实现类
 */
@Service
public class PortalUserAppOperationLogServiceImpl
        extends MyBatisPlusServiceImpl<PortalUserAppOperationLogMapper, PortalUserAppOperationLog>
        implements PortalUserAppOperationLogService {


}
