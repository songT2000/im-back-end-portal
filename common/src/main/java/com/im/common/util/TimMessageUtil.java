package com.im.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.im.common.entity.enums.CustomMessageTypeEnum;
import com.im.common.entity.enums.MsgTypeEnum;
import com.im.common.entity.enums.TiMsgTypeEnum;
import com.im.common.entity.tim.*;
import com.im.common.util.api.im.tencent.entity.param.message.*;

public class TimMessageUtil {
    /**
     * 根据消息类型获取消息实体
     */
    public static Object convertElem(TiMsgBody msgBody){

        Object elem = null;
        if(msgBody.getMsgType().equals(TiMsgTypeEnum.TIMCustomElem)){
            TIMCustomElem msgContent = (TIMCustomElem) msgBody.getMsgContent();
            elem = new TimMessageElemCustom(msgContent);
        }
        if(msgBody.getMsgType().equals(TiMsgTypeEnum.TIMImageElem)){
            TIMImageElem msgContent = (TIMImageElem) msgBody.getMsgContent();
            elem = new TimMessageElemImage(msgContent);
        }
        if(msgBody.getMsgType().equals(TiMsgTypeEnum.TIMFileElem)){
            TIMFileElem msgContent = (TIMFileElem) msgBody.getMsgContent();
            elem = new TimMessageElemFile(msgContent);
        }
        if(msgBody.getMsgType().equals(TiMsgTypeEnum.TIMFaceElem)){
            TIMFaceElem msgContent = (TIMFaceElem) msgBody.getMsgContent();
            elem = new TimMessageElemFace(msgContent);
        }
        if(msgBody.getMsgType().equals(TiMsgTypeEnum.TIMLocationElem)){
            TIMLocationElem msgContent = (TIMLocationElem) msgBody.getMsgContent();
            elem = new TimMessageElemLocation(msgContent);
        }
        if(msgBody.getMsgType().equals(TiMsgTypeEnum.TIMVideoFileElem)){
            TIMVideoFileElem msgContent = (TIMVideoFileElem) msgBody.getMsgContent();
            elem = new TimMessageElemVideo(msgContent);
        }
        if(msgBody.getMsgType().equals(TiMsgTypeEnum.TIMSoundElem)){
            TIMSoundElem msgContent = (TIMSoundElem) msgBody.getMsgContent();
            elem = new TimMessageElemSound(msgContent);
        }
        if(msgBody.getMsgType().equals(TiMsgTypeEnum.TIMTextElem)){
            TIMTextElem msgContent = (TIMTextElem) msgBody.getMsgContent();
            elem = new TimMessageElemText(msgContent);
        }
        return elem;
    }

    /**
     * 根据消息类型获取消息实体
     */
    public static TiMsgBody convertTimElem(TimMessageElem messageElem){
        if(messageElem instanceof TimMessageElemCustom){
            return new TiMsgBody(TiMsgTypeEnum.TIMCustomElem,new TIMCustomElem((TimMessageElemCustom) messageElem));
        }
        if(messageElem instanceof TimMessageElemImage){
            return new TiMsgBody(TiMsgTypeEnum.TIMImageElem,new TIMImageElem((TimMessageElemImage) messageElem));
        }
        if(messageElem instanceof TimMessageElemFile){
            return new TiMsgBody(TiMsgTypeEnum.TIMFileElem,new TIMFileElem((TimMessageElemFile) messageElem));
        }
        if(messageElem instanceof TimMessageElemFace){
            return new TiMsgBody(TiMsgTypeEnum.TIMFaceElem,new TIMFaceElem((TimMessageElemFace) messageElem));
        }
        if(messageElem instanceof TimMessageElemLocation){
            return new TiMsgBody(TiMsgTypeEnum.TIMLocationElem,new TIMLocationElem((TimMessageElemLocation) messageElem));
        }
        if(messageElem instanceof TimMessageElemVideo){
            return new TiMsgBody(TiMsgTypeEnum.TIMVideoFileElem,new TIMVideoFileElem((TimMessageElemVideo) messageElem));
        }
        if(messageElem instanceof TimMessageElemSound){
            return new TiMsgBody(TiMsgTypeEnum.TIMSoundElem,new TIMSoundElem((TimMessageElemSound) messageElem));
        }
        if(messageElem instanceof TimMessageElemText){
            return new TiMsgBody(TiMsgTypeEnum.TIMTextElem,new TIMTextElem((TimMessageElemText) messageElem));
        }
        return null;
    }

    /**
     * 自定义消息转换
     * 自定义消息的内容都是{"businessID":"...","content":"..."}
     */
    public static TimMessageBody customMessageConvert(TimMessageBody messageBody){
        if(messageBody.getMsgType().equals(MsgTypeEnum.TIMCustomElem)){
            TimMessageElemCustom msgContent = (TimMessageElemCustom) messageBody.getMsgContent();
            if(!isJson(msgContent.getData())){
                return messageBody;
            }
            JSONObject jsonObject = JSONObject.parseObject(msgContent.getData());
            String businessID = jsonObject.getString("businessID");
            if(CustomMessageTypeEnum.IMAGE.getVal().equals(businessID)){
                //图像消息
                TimMessageElemImage elemImage = JSON.parseObject(jsonObject.getString("content"), TimMessageElemImage.class);
                return new TimMessageBody(messageBody.getMessageId(),MsgTypeEnum.TIMImageElem,elemImage);
            }
            if(CustomMessageTypeEnum.VIDEO.getVal().equals(businessID)){
                //视频消息
                TimMessageElemVideo elemVideo = JSON.parseObject(jsonObject.getString("content"), TimMessageElemVideo.class);
                return new TimMessageBody(messageBody.getMessageId(),MsgTypeEnum.TIMVideoFileElem,elemVideo);
            }
            if(CustomMessageTypeEnum.FILE.getVal().equals(businessID)){
                //文件消息
                TimMessageElemFile elemFile = JSON.parseObject(jsonObject.getString("content"), TimMessageElemFile.class);
                return new TimMessageBody(messageBody.getMessageId(),MsgTypeEnum.TIMFileElem,elemFile);
            }
            if(CustomMessageTypeEnum.SOUND.getVal().equals(businessID)){
                //语音消息
                TimMessageElemSound elemSound = JSON.parseObject(jsonObject.getString("content"), TimMessageElemSound.class);
                return new TimMessageBody(messageBody.getMessageId(),MsgTypeEnum.TIMSoundElem,elemSound);
            }

        }
        return messageBody;
    }

    public static boolean isJson(String content) {
        try {
            JSONObject.parseObject(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
