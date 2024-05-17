package com.im.common.util.api.im.tencent.entity.param.message;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.entity.tim.TimMessageElemText;
import com.im.common.entity.tim.TimMessageElemVideo;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

/**
 * 视频消息元素
 * <br>
 * 通过服务端集成的 Rest API 接口发送视频消息时，必须填入 VideoUrl、VideoUUID、ThumbUrl、ThumbUUID、ThumbWidth、ThumbHeight、VideoDownloadFlag 和 ThumbDownloadFlag字段。
 * 需保证通过 VideoUrl 能下载到对应视频，通过 ThumbUrl 能下载到对应的视频缩略图。VideoUUID 和 ThumbUUID 字段需填写全局唯一的 String 值，一般填入对应视频和视频缩略图的 MD5 值。
 * 消息接收者可以通过调用 V2TIMVideoElem.getVideoUUID() 和 V2TIMVideoElem.getSnapshotUUID() 分别拿到设置的 UUID 字段，业务 App 可以用这个字段做视频的区分。
 * VideoDownloadFlag 和 ThumbDownloadFlag 字段必须填2
 */
@Data
@NoArgsConstructor
public class TIMVideoFileElem extends TiMsgContent {

    private static final long serialVersionUID = 4223508237981488508L;
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(TimMessageElemVideo.class, TIMVideoFileElem.class, false);

    public TIMVideoFileElem(TimMessageElemVideo elem) {
        BEAN_COPIER.copy(elem, this, null);
    }
    /**
     * 视频下载地址。可通过该 URL 地址直接下载相应视频
     */
    @JSONField(name = "VideoUrl")
    private String videoUrl;
    /**
     * 视频的唯一标识，客户端用于索引视频的键值
     */
    @JSONField(name = "VideoUUID")
    private String videoUUID;
    /**
     * 视频数据大小，单位：字节
     */
    @JSONField(name = "VideoSize")
    private Integer videoSize;
    /**
     * 视频时长，单位：秒
     */
    @JSONField(name = "VideoSecond")
    private Integer videoSecond;
    /**
     * 视频格式，例如 mp4
     */
    @JSONField(name = "VideoFormat")
    private String videoFormat;
    /**
     * 视频下载方式标记。目前 VideoDownloadFlag 取值只能为2，表示可通过VideoUrl字段值的 URL 地址直接下载视频
     */
    @JSONField(name = "VideoDownloadFlag")
    private int videoDownloadFlag = 2;
    /**
     * 视频缩略图下载地址。可通过该 URL 地址直接下载相应视频缩略图
     */
    @JSONField(name = "ThumbUrl")
    private String thumbUrl;
    /**
     * 视频缩略图的唯一标识，客户端用于索引视频缩略图的键值
     */
    @JSONField(name = "ThumbUUID")
    private String thumbUUID;
    /**
     * 缩略图大小，单位：字节
     */
    @JSONField(name = "ThumbSize")
    private Integer thumbSize;
    /**
     * 缩略图宽度，单位为像素
     */
    @JSONField(name = "ThumbWidth")
    private Integer thumbWidth;
    /**
     * 缩略图高度，单位为像素
     */
    @JSONField(name = "ThumbHeight")
    private Integer thumbHeight;
    /**
     * 缩略图格式，例如 JPG、BMP 等
     */
    @JSONField(name = "ThumbFormat")
    private String thumbFormat;
    /**
     * 视频缩略图下载方式标记。目前 ThumbDownloadFlag 取值只能为2，表示可通过ThumbUrl字段值的 URL 地址直接下载视频缩略图
     */
    @JSONField(name = "ThumbDownloadFlag")
    private int thumbDownloadFlag = 2;
}
