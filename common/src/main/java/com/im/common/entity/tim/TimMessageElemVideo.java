package com.im.common.entity.tim;

import com.baomidou.mybatisplus.annotation.TableField;
import com.im.common.entity.base.BaseEntity;
import com.im.common.util.api.im.tencent.entity.param.message.TIMVideoFileElem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.io.Serializable;

/**
 * 视频消息元素
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimMessageElemVideo extends BaseEntity implements Serializable,TimMessageElem {

    private static final long serialVersionUID = 5103284480825330217L;

    public static final BeanCopier BEAN_COPIER = BeanCopier.create(TIMVideoFileElem.class, TimMessageElemVideo.class, false);

    public TimMessageElemVideo(TIMVideoFileElem elem) {
        BEAN_COPIER.copy(elem, this, null);
    }

    /**
     * 视频下载地址。可通过该 URL 地址直接下载相应视频
     */
    private String videoUrl;
    /**
     * 视频的唯一标识，客户端用于索引视频的键值
     */
    @TableField(value = "video_uuid")
    private String videoUUID;
    /**
     * 视频数据大小，单位：字节
     */
    private Integer videoSize;
    /**
     * 视频时长，单位：秒
     */
    private Integer videoSecond;
    /**
     * 视频格式，例如 mp4
     */
    private String videoFormat;
    /**
     * 视频缩略图下载地址。可通过该 URL 地址直接下载相应视频缩略图
     */
    private String thumbUrl;
    /**
     * 视频缩略图的唯一标识，客户端用于索引视频缩略图的键值
     */
    @TableField(value = "thumb_uuid")
    private String thumbUUID;
    /**
     * 缩略图大小，单位：字节
     */
    private Integer thumbSize;
    /**
     * 缩略图宽度，单位为像素
     */
    private Integer thumbWidth;
    /**
     * 缩略图高度，单位为像素
     */
    private Integer thumbHeight;
    /**
     * 缩略图格式，例如 JPG、BMP 等
     */
    private String thumbFormat;
}
