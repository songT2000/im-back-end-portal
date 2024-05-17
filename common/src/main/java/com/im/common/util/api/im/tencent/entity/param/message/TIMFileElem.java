package com.im.common.util.api.im.tencent.entity.param.message;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.entity.tim.TimMessageElemFace;
import com.im.common.entity.tim.TimMessageElemFile;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

/**
 * 文件消息元素
 * <br>通过服务端集成的 Rest API 接口发送文件消息时，需要填入文件的 Url、UUID、Download_Flag 字段。需保证通过该 Url 能下载到对应文件。
 * UUID 字段需填写全局唯一的 String 值，一般填入文件的 MD5 值。消息接收者可以通过调用 V2TIMFileElem.getUUID() 拿到设置的 UUID 字段，
 * 业务 App 可以用这个字段做文件的区分。Download_Flag字段必须填2
 */
@Data
@NoArgsConstructor
public class TIMFileElem extends TiMsgContent {

    private static final long serialVersionUID = 6028086380051438095L;

    public static final BeanCopier BEAN_COPIER = BeanCopier.create(TimMessageElemFile.class, TIMFileElem.class, false);

    public TIMFileElem(TimMessageElemFile elem) {
        BEAN_COPIER.copy(elem, this, null);
    }
    /**
     * 文件下载地址，可通过该 URL 地址直接下载相应文件
     */
    @JSONField(name = "Url")
    private String url;
    /**
     * 文件的唯一标识，客户端用于索引文件的键值
     */
    @JSONField(name = "UUID")
    private String uuid;
    /**
     * 文件数据大小，单位：字节
     */
    @JSONField(name = "FileSize")
    private Integer fileSize;
    /**
     * 文件名称
     */
    @JSONField(name = "FileName")
    private String fileName;
    /**
     * 文件下载方式标记。目前 Download_Flag 取值只能为2，表示可通过Url字段值的 URL 地址直接下载文件
     */
    @JSONField(name = "Download_Flag")
    private int downloadFlag = 2;
}
