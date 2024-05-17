package com.im.common.vo;

import com.im.common.entity.PortalUserEmoji;
import com.im.common.entity.tim.TimFace;
import com.im.common.entity.tim.TimFaceItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@ApiModel
public class PortalUserEmojiVo {

    public PortalUserEmojiVo(PortalUserEmoji portalUserEmoji) {
        this.id = portalUserEmoji.getId();
        this.url = portalUserEmoji.getUrl();
    }
    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    private Long id;
    /**
     * 地址
     */
    @ApiModelProperty(value = "地址")
    private String url;

}
