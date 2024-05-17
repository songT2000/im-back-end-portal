package com.im.common.service.impl;

import com.im.common.entity.SysCacheRefresh;
import com.im.common.mapper.SysCacheRefreshMapper;
import com.im.common.service.SysCacheRefreshService;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 缓存刷新表 服务实现类
 *
 * @author Barry
 * @date 2018/6/8
 */
@Service
public class SysCacheRefreshServiceImpl
        extends MyBatisPlusServiceImpl<SysCacheRefreshMapper, SysCacheRefresh>
        implements SysCacheRefreshService {
}