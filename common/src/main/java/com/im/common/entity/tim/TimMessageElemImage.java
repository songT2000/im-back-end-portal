package com.im.common.entity.tim;

import com.im.common.entity.base.BaseEntity;
import com.im.common.entity.enums.TiImageFormatEnum;
import com.im.common.util.EnumUtil;
import com.im.common.util.api.im.tencent.entity.param.message.TIMImageElem;
import com.im.common.util.api.im.tencent.entity.param.message.TIMImageElemInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 图像消息元素
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TimMessageElemImage extends BaseEntity implements Serializable, TimMessageElem {

    private static final long serialVersionUID = -6797620266505808272L;

    /**
     * 图片的唯一标识，客户端用于索引图片的键值
     */
    private String uuid;
    /**
     * 图片格式。JPG，GIF，PNG，BMP，其他
     */
    private TiImageFormatEnum imageFormat;
    /**
     * 原图图片数据大小，单位：字节
     */
    private Integer originSize;
    /**
     * 原图图片宽度，单位为像素
     */
    private Integer originWidth;
    /**
     * 原图图片高度，单位为像素
     */
    private Integer originHeight;
    /**
     * 原图图片下载地址
     */
    private String originUrl;

    /**
     * 大图图片数据大小，单位：字节
     */
    private Integer bigSize;
    /**
     * 大图图片宽度，单位为像素（腾讯IM有bug，返回是0）
     */
    private Integer bigWidth;
    /**
     * 大图图片高度，单位为像素（腾讯IM有bug，返回是0）
     */
    private Integer bigHeight;
    /**
     * 大图图片下载地址
     */
    private String bigUrl;

    /**
     * 缩略图图片数据大小，单位：字节
     */
    private Integer thumbSize;
    /**
     * 缩略图图片宽度，单位为像素
     */
    private Integer thumbWidth;
    /**
     * 缩略图图片高度，单位为像素
     */
    private Integer thumbHeight;
    /**
     * 缩略图图片下载地址
     */
    private String thumbUrl;

    public TimMessageElemImage(TIMImageElem timImageElem) {
        this.setUuid(timImageElem.getUuid());
        this.setImageFormat(EnumUtil.valueOfIEnum(TiImageFormatEnum.class, String.valueOf(timImageElem.getImageFormat())));
        for (TIMImageElemInfo info : timImageElem.getImageInfoArray()) {
            if (info.getType() == TIMImageElemInfo.ORIGIN) {
                this.setOriginUrl(info.getUrl());
                this.setOriginHeight(info.getHeight());
                this.setOriginSize(info.getSize());
                this.setOriginWidth(info.getWidth());
            }
            if (info.getType() == TIMImageElemInfo.BIG) {
                this.setBigUrl(info.getUrl());
                this.setBigHeight(info.getHeight());
                this.setBigSize(info.getSize());
                this.setBigWidth(info.getWidth());
            }
            if (info.getType() == TIMImageElemInfo.THUMB) {
                this.setThumbUrl(info.getUrl());
                this.setThumbHeight(info.getHeight());
                this.setThumbSize(info.getSize());
                this.setThumbWidth(info.getWidth());
            }
        }
    }
}
