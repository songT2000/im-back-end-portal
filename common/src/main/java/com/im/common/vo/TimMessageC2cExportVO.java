package com.im.common.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.im.common.entity.enums.MsgTypeEnum;
import com.im.common.entity.tim.TimMessageBody;
import com.im.common.entity.tim.TimMessageElemText;
import com.im.common.util.CollectionUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.easyexcel.EasyexcelEnumConverter;
import com.im.common.util.easyexcel.EasyexcelLocalDateTimeConverter;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDateTime;

/**
 * 单聊信息导出
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimMessageC2cExportVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(TimMessageC2cVO.class, TimMessageC2cExportVO.class, false);

    @ApiModelProperty("ID")
    @ExcelIgnore
    private Long id;

    @ApiModelProperty("消息发送时间")
    @ExcelProperty(value = "消息发送时间", index = 0, converter = EasyexcelLocalDateTimeConverter.class)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sendTime;

    @ApiModelProperty("发言人昵称")
    @ExcelProperty(value = "发言人昵称", index = 1)
    private String fromUserNickname;

    @ApiModelProperty("接收人昵称")
    @ExcelProperty(value = "接收人昵称", index = 2)
    private String toUserNickname;

    @ApiModelProperty("消息格式")
    @ExcelProperty(value = "消息格式", index = 3, converter = EasyexcelEnumConverter.class)
    private MsgTypeEnum msgType;

    @ApiModelProperty("消息内容")
    @ExcelProperty(value = "消息内容", index = 4)
    private String content;

    public TimMessageC2cExportVO(TimMessageC2cVO e) {
        BEAN_COPIER.copy(e, this, null);
        this.fromUserNickname = UserUtil.getUserNicknameByIdFromLocal(e.getFromUserId());
        this.toUserNickname = UserUtil.getUserNicknameByIdFromLocal(e.getToUserId());
        if (CollectionUtil.isNotEmpty(e.getMsgBody())) {
            TimMessageBody body = e.getMsgBody().get(0);//系统只支持一个messageBody
            this.msgType = body.getMsgType();
            if (body.getMsgType().equals(MsgTypeEnum.TIMTextElem)) {
                TimMessageElemText elemText = (TimMessageElemText) body.getMsgContent();
                this.content = elemText.getText();
            } else {
                this.content = StrUtil.SLASH;
            }
        }
    }
}
