package com.im.common.vo;

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
public class TimFaceVo {

    public TimFaceVo(TimFace timFace, List<TimFaceItem> items) {
        this.timFaceId = timFace.getId();
        this.faceName = timFace.getFaceName();
        this.chatPanelIcon = timFace.getChatPanelIcon();
        this.items = items.stream().map(TimFaceItemVo::new).collect(Collectors.toList());
        this.count = items.size();
    }
    /**
     * 专辑ID
     */
    @ApiModelProperty(value = "专辑ID")
    private Long timFaceId;
    /**
     * 专辑名称
     */
    @ApiModelProperty(value = "专辑名称")
    private String faceName;
    /**
     * 聊天面板图标，50*50
     */
    @ApiModelProperty(value = "聊天面板图标")
    private String chatPanelIcon;

    @ApiModelProperty(value = "表情数量")
    private Integer count;

    /**
     * 专辑所含表情集合
     */
    @ApiModelProperty(value = "专辑所含表情集合")
    private List<TimFaceItemVo> items;


    @Data
    @NoArgsConstructor
    @ApiModel
    static class TimFaceItemVo {

        public TimFaceItemVo(TimFaceItem timFaceItem) {
            this.faceId = timFaceItem.getId();
            this.faceIndex = timFaceItem.getFaceIndex();
            this.faceUrl = timFaceItem.getFaceUrl();
        }

        /**
         * 表情包ID
         */
        @ApiModelProperty(value = "表情包ID")
        private Long faceId;
        /**
         * 表情包序号
         */
        @ApiModelProperty(value = "表情包序号")
        private Integer faceIndex;
        /**
         * 主图地址 240*240
         */
        @ApiModelProperty(value = "表情包地址，用于发送")
        private String faceUrl;
    }

}
