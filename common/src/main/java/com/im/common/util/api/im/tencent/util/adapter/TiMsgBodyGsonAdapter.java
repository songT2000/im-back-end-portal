// package com.im.common.util.api.im.tencent.util.adapter;
//
// import com.google.gson.*;
// import com.im.common.entity.enums.TiMsgTypeEnum;
// import com.im.common.util.api.im.tencent.entity.param.message.*;
// import com.im.common.util.api.im.tencent.util.GsonHelper;
// import com.im.common.util.api.im.tencent.util.TiGsonBuilder;
//
// import java.lang.reflect.Type;
//
// public class TiMsgBodyGsonAdapter implements JsonDeserializer<TiMsgBody> {
//     private static final String MsgType = "MsgType";
//     private static final String MsgContent = "MsgContent";
//     @Override
//     public TiMsgBody deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
//         JsonObject jsonObject = jsonElement.getAsJsonObject();
//         TiMsgBody msgBody = new TiMsgBody();
//         if (jsonObject.get(MsgType) != null && !jsonObject.get(MsgType).isJsonNull()) {
//             msgBody.setMsgType(TiMsgTypeEnum.getByValue(GsonHelper.getAsString(jsonObject.get(MsgType))));
//         }
//         JsonElement msgContentJsonElement = jsonObject.get(MsgContent);
//         if (msgContentJsonElement != null && !msgContentJsonElement.isJsonNull()) {
//             if(msgBody.getMsgType().equals(TiMsgTypeEnum.TIMTextElem)){
//                 msgBody.setMsgContent(TiGsonBuilder.create().fromJson(msgContentJsonElement, TIMTextElem.class));
//             }
//             if(msgBody.getMsgType().equals(TiMsgTypeEnum.TIMCustomElem)){
//                 msgBody.setMsgContent(TiGsonBuilder.create().fromJson(msgContentJsonElement, TIMCustomElem.class));
//             }
//             if(msgBody.getMsgType().equals(TiMsgTypeEnum.TIMFaceElem)){
//                 msgBody.setMsgContent(TiGsonBuilder.create().fromJson(msgContentJsonElement, TIMFaceElem.class));
//             }
//             if(msgBody.getMsgType().equals(TiMsgTypeEnum.TIMFileElem)){
//                 msgBody.setMsgContent(TiGsonBuilder.create().fromJson(msgContentJsonElement, TIMFileElem.class));
//             }
//             if(msgBody.getMsgType().equals(TiMsgTypeEnum.TIMImageElem)){
//                 msgBody.setMsgContent(TiGsonBuilder.create().fromJson(msgContentJsonElement, TIMImageElem.class));
//
//             }
//             if(msgBody.getMsgType().equals(TiMsgTypeEnum.TIMLocationElem)){
//                 msgBody.setMsgContent(TiGsonBuilder.create().fromJson(msgContentJsonElement, TIMLocationElem.class));
//             }
//             if(msgBody.getMsgType().equals(TiMsgTypeEnum.TIMSoundElem)){
//                 msgBody.setMsgContent(TiGsonBuilder.create().fromJson(msgContentJsonElement, TIMSoundElem.class));
//             }
//             if(msgBody.getMsgType().equals(TiMsgTypeEnum.TIMVideoFileElem)){
//                 msgBody.setMsgContent(TiGsonBuilder.create().fromJson(msgContentJsonElement, TIMVideoFileElem.class));
//             }
//
//         }
//
//
//         return msgBody;
//     }
// }
