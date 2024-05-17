package com.im.common.mapper;

import com.im.common.entity.SysBackupConfig;
import com.im.common.util.mybatis.mapper.MyBatisPlusMapper;
import org.springframework.stereotype.Repository;

/**
 * 数据备份配置表 Mapper 接口
 *
 * @author Barry
 * @date 2020-01-04
 */
@Repository
public interface SysBackupConfigMapper extends MyBatisPlusMapper<SysBackupConfig> {
}
