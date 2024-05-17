// package com.im.common.util.api.im.tencent.util.adapter;
//
// import com.google.gson.*;
// import com.im.common.util.api.im.tencent.entity.GsonIntegerEnum;
//
// import java.lang.reflect.Type;
//
// public class GsonIntegerEnumTypeAdapter<T> implements JsonSerializer<T> , JsonDeserializer<T> {
//
//     private final GsonIntegerEnum<T> gsonEnum;
//
//     public GsonIntegerEnumTypeAdapter(GsonIntegerEnum<T> gsonEnum) {
//         this.gsonEnum = gsonEnum;
//     }
//
//     @Override
//     public JsonElement serialize(T src, Type type, JsonSerializationContext jsonSerializationContext) {
//         if (src instanceof GsonIntegerEnum) {
//             return new JsonPrimitive(((GsonIntegerEnum) src).serialize());
//         }
//         return null;
//     }
//
//     @Override
//     public T deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
//         if (null != json) {
//             return gsonEnum.deserialize(json.getAsInt());
//         }
//         return null;
//     }
// }
