// package com.im.common.util.api.im.tencent.util.adapter;
//
// import com.google.gson.*;
// import com.im.common.util.api.im.tencent.entity.TiCustomItem;
// import com.im.common.util.api.im.tencent.util.GsonHelper;
//
// import java.lang.reflect.Type;
//
// public class TiCustomItemGsonAdapter implements JsonDeserializer<TiCustomItem> {
//     private static final String Tag = "Tag";
//     private static final String Value = "Value";
//     @Override
//     public TiCustomItem deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
//         JsonObject jsonObject = jsonElement.getAsJsonObject();
//         TiCustomItem item = new TiCustomItem();
//         if (jsonObject.get(Tag) != null && !jsonObject.get(Tag).isJsonNull()) {
//             item.setTag(GsonHelper.getAsString(jsonObject.get(Tag)));
//         }
//         if (jsonObject.get(Value) != null && !jsonObject.get(Value).isJsonNull()) {
//             String string = GsonHelper.getAsString(jsonObject.get(Value));
//             if(string != null){
//                 if(isNumeric(string)){
//                     item.setValue(Long.parseLong(string));
//                 }else{
//                     item.setValue(string);
//                 }
//             }
//
//         }
//
//         return item;
//     }
//
//     public static boolean isNumeric(String str) {
//         for (int i = 0; i < str.length(); i++) {
//             System.out.println(str.charAt(i));
//             if (!Character.isDigit(str.charAt(i))) {
//                 return false;
//             }
//         }
//         return true;
//     }
// }
