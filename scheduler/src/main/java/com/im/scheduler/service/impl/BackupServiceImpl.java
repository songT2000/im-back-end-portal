package com.im.scheduler.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSON;
import com.im.common.entity.SysBackupConfig;
import com.im.common.mapper.BackupMapper;
import com.im.common.service.SysBackupConfigService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.DateTimeUtil;
import com.im.common.util.NumberUtil;
import com.im.scheduler.service.BackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 系统数据备份 实现类
 *
 * @author Barry
 * @date 2020-01-01
 */
@Service
public class BackupServiceImpl implements BackupService {
    private static final Log LOG = LogFactory.get();

    private SysBackupConfigService backupConfigService;
    private BackupMapper backupMapper;

    @Autowired
    public void setBackupConfigService(SysBackupConfigService backupConfigService) {
        this.backupConfigService = backupConfigService;
    }

    @Autowired
    public void setBackupMapper(BackupMapper backupMapper) {
        this.backupMapper = backupMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void backupAllData() {
        List<SysBackupConfig> configList = backupConfigService.listByEnabled();
        if (CollectionUtil.isEmpty(configList)) {
            return;
        }

        configList.forEach(this::backupData);
    }

    private void backupData(SysBackupConfig config) {
        try {
            // 删除备份数据
            deleteData(config.getBackupTableName(), config.getBackupKeepDay());

            // 移动实时数据到备份数据
            moveRealDataToBackup(config.getRealTableName(), config.getBackupTableName(), config.getRealKeepDay());

            // 删除实时数据
            deleteData(config.getRealTableName(), config.getRealKeepDay());
        } catch (Exception e) {
            LOG.error(e, "备份数据发生异常{}", JSON.toJSONString(config));
        }
    }

    private long deleteData(String tableName, Integer keepDay) {
        if (StrUtil.isNotBlank(tableName) && NumberUtil.isGreatThenZero(keepDay)) {
            String createTime = DateTimeUtil.minusDaysFromNowToDateTimeStr(keepDay);
            Long affectedRows = backupMapper.deleteDataByCreateTime(tableName, createTime);

            LOG.info("删除{}表{}时间之前共计{}条数据", tableName, createTime, affectedRows);

            return Optional.ofNullable(affectedRows).orElse(0L);
        }

        return 0;
    }

    private long moveRealDataToBackup(String realTableName, String backupTableName, Integer realKeepDay) {
        if (StrUtil.isBlank(realTableName) || StrUtil.isBlank(backupTableName) || !NumberUtil.isGreatThenZero(realKeepDay)) {
            return 0;
        }

        String createTime = DateTimeUtil.minusDaysFromNowToDateTimeStr(realKeepDay);
        Long affectedRows = backupMapper.moveRealDataToBackupByCreateTime(realTableName, backupTableName, createTime);

        LOG.info("转移{}表到{}表{}时间之前共计{}条数据", realTableName, backupTableName, createTime, affectedRows);

        return Optional.ofNullable(affectedRows).orElse(0L);
    }
}
