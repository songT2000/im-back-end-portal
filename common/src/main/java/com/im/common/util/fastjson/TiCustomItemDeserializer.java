package com.im.common.util.fastjson;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.im.common.util.StrUtil;
import com.im.common.util.api.im.tencent.entity.TiCustomItem;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TiCustomItemDeserializer implements ObjectDeserializer {

    public static final TiCustomItemDeserializer INSTANCE = new TiCustomItemDeserializer();

    private static final String Tag = "Tag";
    private static final String Value = "Value";

    @Override
    public TiCustomItem deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        TiCustomItem item = new TiCustomItem();
        JSONObject jsonObject = parser.parseObject();
        String tag = jsonObject.getString(Tag);
        item.setTag(tag);

        Object targetObj = jsonObject.get(Value);
        if( targetObj instanceof JSONArray){
            JSONArray jsonArray = (JSONArray) targetObj;

            List<Object> list = new ArrayList<>(jsonArray);
            item.setValue(StrUtil.join(StrUtil.COMMA,list));
        }else if( targetObj instanceof JSONObject ){
            item.setValue(targetObj);
        }else {
            item.setValue(targetObj);
        }

        return item;
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}
