package com.im.common.service;

import com.im.common.entity.TimMessageBackupFile;
import com.im.common.entity.tim.TimMessageC2c;
import com.im.common.entity.tim.TimMessageGroup;
import com.im.common.util.mybatis.service.MyBatisPlusService;

import java.util.List;

/**
 * 定时下载消息，用于备份
 */
public interface TimMessageDownloadService extends MyBatisPlusService<TimMessageBackupFile> {

    /**
     * 下载单聊消息
     * @param time      需要下载的时间，格式yyyyMMddHH
     * @return          该时间端内的单聊消息记录
     */
    List<TimMessageC2c> downloadC2cMessage(String time);

    /**
     * 下载群聊消息
     * @param time      需要下载的时间，格式yyyyMMddHH
     * @return          该时间端内的单聊消息记录
     */
    List<TimMessageGroup> downloadGroupMessage(String time);

}
