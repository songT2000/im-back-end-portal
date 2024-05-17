package com.im.common.util.api.im.tencent.entity.param.message;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 图像消息元素
 * <br>通过服务端集成的 Rest API 接口发送图像消息时，必须填入图像的以下字段：URL、UUID、Width、Height。需保证通过 URL 能下载到对应图像。
 * 可据此 获取图片基本信息 并 处理图片。Width 和 Height 分别为图片的宽度和高度，单位为像素。UUID 字段需填写全局唯一的 String 值，一般填入图片的 MD5 值。
 * 消息接收者通过调用 V2TIMImageElem.getImageList() 拿到 V2TIMImage 对象，然后通过调用V2TIMImage.getUUID() 拿到设置的 UUID 字段，业务 App 可以用这个字段做图片的区分。
 */
@NoArgsConstructor
@Data
public class TIMImageElemInfo implements Serializable {
    public static final int ORIGIN = 1;
    public static final int BIG = 2;
    public static final int THUMB = 3;
    /**
     * 图片类型： 1-原图，2-大图，3-缩略图
     * <br>
     */
    @JSONField(name = "Type")
    private Integer type;
    /**
     * 图片数据大小，单位：字节
     */
    @JSONField(name = "Size")
    private Integer size;
    /**
     * 图片宽度，单位为像素
     */
    @JSONField(name = "Width")
    private Integer width;
    /**
     * 图片高度，单位为像素
     */
    @JSONField(name = "Height")
    private Integer height;
    /**
     * 图片下载地址
     */
    @JSONField(name = "URL")
    private String url;
}
