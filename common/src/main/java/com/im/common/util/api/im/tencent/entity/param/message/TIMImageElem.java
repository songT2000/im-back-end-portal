package com.im.common.util.api.im.tencent.entity.param.message;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.entity.tim.TimMessageElemImage;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 图像消息元素
 * <br>通过服务端集成的 Rest API 接口发送图像消息时，必须填入图像的以下字段：URL、UUID、Width、Height。需保证通过 URL 能下载到对应图像。
 * 可据此 获取图片基本信息 并 处理图片。Width 和 Height 分别为图片的宽度和高度，单位为像素。UUID 字段需填写全局唯一的 String 值，一般填入图片的 MD5 值。
 * 消息接收者通过调用 V2TIMImageElem.getImageList() 拿到 V2TIMImage 对象，然后通过调用V2TIMImage.getUUID() 拿到设置的 UUID 字段，业务 App 可以用这个字段做图片的区分。
 */
@NoArgsConstructor
@Data
public class TIMImageElem extends TiMsgContent {

    public static final int JPG = 1;
    public static final int GIF = 2;
    public static final int PNG = 3;
    public static final int BMP = 4;
    public static final int OTHER = 255;
    private static final long serialVersionUID = -4619815168367194991L;
    /**
     * 图片的唯一标识，客户端用于索引图片的键值
     */
    @JSONField(name = "UUID")
    private String uuid;
    /**
     * 图片格式。JPG = 1，GIF = 2，PNG = 3，BMP = 4，其他 = 255
     * <br>
     */
    @JSONField(name = "ImageFormat")
    private Integer imageFormat;
    /**
     * 原图、缩略图或者大图下载信息
     */
    @JSONField(name = "ImageInfoArray")
    private List<TIMImageElemInfo> imageInfoArray;

    public TIMImageElem(TimMessageElemImage elem) {
        this.uuid = elem.getUuid();
        this.imageFormat = Integer.valueOf(elem.getImageFormat().getVal());
        TIMImageElemInfo origin = new TIMImageElemInfo();
        origin.setType(TIMImageElemInfo.ORIGIN);
        origin.setSize(elem.getOriginSize());
        origin.setWidth(elem.getOriginWidth());
        origin.setUrl(elem.getOriginUrl());
        origin.setHeight(elem.getOriginHeight());
        TIMImageElemInfo big = new TIMImageElemInfo();
        big.setType(TIMImageElemInfo.BIG);
        big.setSize(elem.getBigSize());
        big.setWidth(elem.getBigWidth());
        big.setUrl(elem.getBigUrl());
        big.setHeight(elem.getBigHeight());
        TIMImageElemInfo thumb = new TIMImageElemInfo();
        thumb.setType(TIMImageElemInfo.THUMB);
        thumb.setSize(elem.getThumbSize());
        thumb.setWidth(elem.getThumbWidth());
        thumb.setUrl(elem.getThumbUrl());
        thumb.setHeight(elem.getBigHeight());
        this.setImageInfoArray(ListUtil.of(origin, big, thumb));
    }
}
