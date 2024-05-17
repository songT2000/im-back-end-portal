package com.im.common.entity;

import cn.hutool.core.date.DatePattern;
import com.im.common.entity.base.BaseEntity;
import com.im.common.entity.enums.ChatTypeEnum;
import com.im.common.util.api.im.tencent.entity.result.operation.TiDownloadMsgFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 消息记录下载备份文件记录
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimMessageBackupFile extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 725020787353606194L;

    public TimMessageBackupFile(TiDownloadMsgFile item) {
        this.expireTime = LocalDateTime.parse(item.getExpireTime(), DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN));
        this.url = item.getUrl();
        this.fileMd5 = item.getFileMD5();
        this.fileSize = item.getFileSize();
        this.gzipMd5 = item.getGzipMD5();
        this.gzipSize = item.getGzipSize();
    }

    /**
     * 聊天类型
     */
    private ChatTypeEnum chatType;
    /**
     * 所属时间，格式：yyyyMMddHH
     */
    private String belongTime;
    /**
     * 消息条数
     */
    private int messageCount;
    /**
     * 消息记录文件下载地址
     */
    private String url;
    /**
     * 下载地址过期时间，请在过期前进行下载，若地址失效，请通过该接口重新获取
     */
    private LocalDateTime expireTime;
    /**
     * GZip 压缩前的文件大小（单位 Byte）
     */
    private Long fileSize;
    /**
     * GZip 压缩前的文件 MD5
     */
    private String fileMd5;
    /**
     * GZip 压缩后的文件大小（单位 Byte）
     */
    private Long gzipSize;
    /**
     * GZip 压缩后的文件 MD5
     */
    private String gzipMd5;
}
