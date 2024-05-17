package com.im.common.mapper;

import com.im.common.entity.AppVersion;
import com.im.common.util.mybatis.mapper.MyBatisPlusMapper;
import org.springframework.stereotype.Repository;

/**
 * 应用版本管理 Mapper 接口
 */
@Repository
public interface AppVersionMapper extends MyBatisPlusMapper<AppVersion> {
}
