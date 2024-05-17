package com.im.common.util.fastjson;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.im.common.entity.enums.TiMsgTypeEnum;
import com.im.common.util.EnumUtil;
import com.im.common.util.MessageEncryptUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.api.im.tencent.entity.param.message.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TiMsgBodyDeserializer implements ObjectDeserializer {

    public static final TiMsgBodyDeserializer INSTANCE = new TiMsgBodyDeserializer();
    private static final String MsgType = "MsgType";
    private static final String MsgContent = "MsgContent";
    private static final String ImageFormat = "ImageFormat";
    private static final String UUID = "UUID";
    private static final String ImageInfoArray = "ImageInfoArray";
    private static final String Type = "Type";
    private static final String URL = "URL";
    private static final String Size = "Size";
    private static final String Height = "Height";
    private static final String Width = "Width";
    private static final Log LOG = LogFactory.get();

    @Override
    public TiMsgBody deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        TiMsgBody item = new TiMsgBody();
        TiMsgContent content = null;
        JSONObject jsonObject = parser.parseObject();
        String string = jsonObject.getString(MsgType);
        TiMsgTypeEnum typeEnum = EnumUtil.valueOfIEnum(TiMsgTypeEnum.class, string);
        if (typeEnum == null) {
            return null;
        }
        switch (typeEnum) {
            case TIMTextElem:
                TIMTextElem textElem = jsonObject.getObject(MsgContent, TIMTextElem.class);
                //解密
                String txt;
                try {
                    txt = MessageEncryptUtil.decrypt(textElem.getText());
                } catch (Exception e) {
                    LOG.warn("消息解密失败，消息内容：" + textElem.getText());
                    txt = StrUtil.EMPTY;
                }
                textElem.setText(txt);
                content = textElem;
                break;
            case TIMCustomElem:
                TIMCustomElem customElem = jsonObject.getObject(MsgContent, TIMCustomElem.class);
                //解密
                try {
                    customElem.setData(MessageEncryptUtil.decrypt(customElem.getData()));
                } catch (Exception e) {
                    LOG.warn("消息解密失败，消息内容：" + customElem.getData());
                }
                content = customElem;
                break;
            case TIMFaceElem:
                content = jsonObject.getObject(MsgContent, TIMFaceElem.class);
                break;
            case TIMFileElem:
                content = jsonObject.getObject(MsgContent, TIMFileElem.class);
                break;
            case TIMImageElem:
                JSONObject j = jsonObject.getJSONObject(MsgContent);
                String uuid = j.getString(UUID);
                JSONArray jsonArray = j.getJSONArray(ImageInfoArray);
                TIMImageElem imageElem = new TIMImageElem();
                imageElem.setUuid(uuid);
                imageElem.setImageFormat(j.getInteger(ImageFormat));
                List<TIMImageElemInfo> infos = new ArrayList<>();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject o = jsonArray.getJSONObject(i);
                    TIMImageElemInfo info = new TIMImageElemInfo();
                    info.setType(o.getInteger(Type));
                    info.setHeight(o.getInteger(Height));
                    info.setWidth(o.getInteger(Width));
                    info.setSize(o.getInteger(Size));
                    info.setUrl(o.getString(URL));
                    infos.add(info);
                }
                imageElem.setImageInfoArray(infos);
                content = imageElem;
                break;
            case TIMLocationElem:
                content = jsonObject.getObject(MsgContent, TIMLocationElem.class);
                break;
            case TIMSoundElem:
                content = jsonObject.getObject(MsgContent, TIMSoundElem.class);
                break;
            case TIMVideoFileElem:
                content = jsonObject.getObject(MsgContent, TIMVideoFileElem.class);
                break;
            default:
                break;
        }
        item.setMsgType(typeEnum);
        item.setMsgContent(content);
        return item;
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}
