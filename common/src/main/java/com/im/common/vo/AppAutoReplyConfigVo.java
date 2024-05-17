package com.im.common.vo;

import com.im.common.entity.AppAutoReplyConfig;
import com.im.common.entity.enums.MsgTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

@Data
@NoArgsConstructor
@ApiModel
public class AppAutoReplyConfigVo {

    private static final BeanCopier BEAN_COPIER = BeanCopier.create(AppAutoReplyConfig.class, AppAutoReplyConfigVo.class, false);

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "管理员账号集合，多个使用逗号分割")
    private String usernames;

    /**
     * 消息类型，TIMTextElem图片消息，TIMImageElem文本消息
     */
    @ApiModelProperty(value = "管理员ids，多个使用逗号分割")
    private MsgTypeEnum msgType;

    /**
     * 自动回复内容
     */
    @ApiModelProperty(value = "自动回复内容")
    private String content;

    /**
     * 自动回复开始时间,格式：HH:mm:ss
     **/
    @ApiModelProperty(value = "自动回复开始时间,格式：HH:mm:ss")
    private String startTime;

    /**
     * 自动回复结束时间,格式：HH:mm:ss
     */
    @ApiModelProperty(value = "自动回复结束时间,格式：HH:mm:ss")
    private String endTime;

    public AppAutoReplyConfigVo(AppAutoReplyConfig e) {
        BEAN_COPIER.copy(e, this, null);
    }


}
