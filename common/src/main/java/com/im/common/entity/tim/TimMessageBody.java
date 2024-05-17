package com.im.common.entity.tim;

import com.im.common.entity.enums.MsgTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimMessageBody implements Serializable {

    private static final long serialVersionUID = 6778405502096652523L;

    private Long messageId;

    private MsgTypeEnum msgType;

    private TimMessageElem msgContent;

}
