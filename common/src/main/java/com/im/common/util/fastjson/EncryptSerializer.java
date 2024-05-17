package com.im.common.util.fastjson;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.im.common.util.MessageEncryptUtil;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 加密序列化
 */
public class EncryptSerializer extends ToStringSerializer {

    public static final EncryptSerializer INSTANCE = new EncryptSerializer();

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
                      int features) throws IOException {
        SerializeWriter out = serializer.out;

        if (object == null) {
            out.writeNull();
            return;
        }

        String strVal = object.toString();
        out.writeString(MessageEncryptUtil.encrypt(strVal));
    }

}
