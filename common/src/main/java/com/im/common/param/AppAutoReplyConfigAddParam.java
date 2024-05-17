package com.im.common.param;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.im.common.entity.enums.MsgTypeEnum;
import com.im.common.entity.enums.TiImageFormatEnum;
import com.im.common.entity.tim.TimMessageElemImage;
import com.im.common.util.FileUtil;
import com.im.common.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 新增自动回复配置
 */
@Data
@NoArgsConstructor
@ApiModel
public class AppAutoReplyConfigAddParam {

    @NotBlank
    @ApiModelProperty(value = "管理员账号集合，多个使用逗号分割", required = true)
    private String usernames;


    /**
     * 消息类型，TIMTextElem图片消息，TIMImageElem文本消息
     */
    @NotNull
    @ApiModelProperty(value = "消息类型，TIMTextElem图片消息，TIMImageElem文本消息", required = true)
    private MsgTypeEnum msgType;

    /**
     * 自动回复内容
     */
    @NotBlank
    @ApiModelProperty(value = "自动回复内容", required = true)
    private String content;

    /**
     * 自动回复开始时间,格式：HH:mm:ss
     **/
    @NotBlank
    @ApiModelProperty(value = "自动回复开始时间,格式：HH:mm:ss", required = true)
    private String startTime;

    /**
     * 自动回复结束时间,格式：HH:mm:ss
     */
    @NotBlank
    @ApiModelProperty(value = "自动回复结束时间,格式：HH:mm:ss", required = true)
    private String endTime;

    @ApiModelProperty(value = "图片大小")
    private Integer imageSize;

    @ApiModelProperty(value = "图片宽")
    private Integer imageWidth;

    @ApiModelProperty(value = "图片高")
    private Integer imageHeight;

    public String getNote() {
        if (this.getMsgType().equals(MsgTypeEnum.TIMImageElem)) {
            TimMessageElemImage elemImage = new TimMessageElemImage();
            elemImage.setUuid(IdUtil.fastSimpleUUID());
            String suffix = FileUtil.getFileSuffix(this.getContent());
            if (suffix.equals(ImgUtil.IMAGE_TYPE_JPG) || suffix.equals(ImgUtil.IMAGE_TYPE_JPEG)) {
                elemImage.setImageFormat(TiImageFormatEnum.JPG);
            }
            if (suffix.equals(ImgUtil.IMAGE_TYPE_PNG)) {
                elemImage.setImageFormat(TiImageFormatEnum.PNG);
            }
            elemImage.setBigHeight(this.getImageHeight());
            elemImage.setBigWidth(this.getImageWidth());
            elemImage.setBigSize(this.getImageSize());
            elemImage.setBigUrl(this.getContent());

            elemImage.setOriginHeight(this.getImageHeight());
            elemImage.setOriginWidth(this.getImageWidth());
            elemImage.setOriginSize(this.getImageSize());
            elemImage.setOriginUrl(this.getContent());

            elemImage.setThumbHeight(this.getImageHeight());
            elemImage.setThumbWidth(this.getImageWidth());
            elemImage.setThumbSize(this.getImageSize());
            elemImage.setThumbUrl(this.getContent());

            return JSON.toJSONString(elemImage);
        }
        return StrUtil.EMPTY;
    }
}
