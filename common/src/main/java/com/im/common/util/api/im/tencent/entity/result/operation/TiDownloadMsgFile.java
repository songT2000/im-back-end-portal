package com.im.common.util.api.im.tencent.entity.result.operation;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 消息记录文件下载信息
 */
@Data
@NoArgsConstructor
public class TiDownloadMsgFile implements Serializable {

    /**
     * 消息记录文件下载地址
     */
    @JSONField(name = "URL")
    private String url;
    /**
     * 下载地址过期时间，请在过期前进行下载，若地址失效，请通过该接口重新获取
     */
    @JSONField(name = "ExpireTime")
    private String expireTime;
    /**
     * GZip 压缩前的文件大小（单位 Byte）
     */
    @JSONField(name = "FileSize")
    private Long fileSize;
    /**
     * GZip 压缩前的文件 MD5
     */
    @JSONField(name = "FileMD5")
    private String fileMD5;
    /**
     * GZip 压缩后的文件大小（单位 Byte）
     */
    @JSONField(name = "GzipSize")
    private Long gzipSize;
    /**
     * GZip 压缩后的文件 MD5
     */
    @JSONField(name = "GzipMD5")
    private String gzipMD5;
}
