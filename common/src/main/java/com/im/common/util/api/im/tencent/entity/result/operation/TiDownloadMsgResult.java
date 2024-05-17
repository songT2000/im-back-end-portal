package com.im.common.util.api.im.tencent.entity.result.operation;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 下载最近消息记录
 */
@Data
@NoArgsConstructor
public class TiDownloadMsgResult extends TiBaseResult {

    /**
     * 消息记录文件下载信息
     */
    @JSONField(name = "File")
    private List<TiDownloadMsgFile> fileList;

}
