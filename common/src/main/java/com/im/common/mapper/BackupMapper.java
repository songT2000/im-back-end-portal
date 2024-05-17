package com.im.common.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 系统数据备份
 *
 * @author Barry
 * @date 2020-01-01
 */
@Repository
public interface BackupMapper {
    /**
     * 根据创建时间删除表数据
     *
     * @param tableName  表名
     * @param createTime 创建时间，yyyy-MM-dd HH:mm:ss，小于等于
     * @return 影响条数
     */
    Long deleteDataByCreateTime(@Param("tableName") String tableName,
                                @Param("createTime") String createTime);

    /**
     * 根据创建时间把实时数据移到备份表
     *
     * @param realTableName   实时表名
     * @param backupTableName 备份表名
     * @param createTime      创建时间，yyyy-MM-dd HH:mm:ss，小于等于
     * @return 影响条数
     */
    Long moveRealDataToBackupByCreateTime(@Param("realTableName") String realTableName,
                                          @Param("backupTableName") String backupTableName,
                                          @Param("createTime") String createTime);
}
