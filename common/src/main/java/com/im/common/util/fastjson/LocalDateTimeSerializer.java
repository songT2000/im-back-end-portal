package com.im.common.util.fastjson;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.im.common.util.DateTimeUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;

/**
 * 日期时间序列化
 *
 * @author Barry
 * @date 2019-10-12
 */
public class LocalDateTimeSerializer implements ObjectSerializer {
    public static final LocalDateTimeSerializer INSTANCE = new LocalDateTimeSerializer();

    public LocalDateTimeSerializer() {
    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull();
        } else {
            LocalDateTime localDateTime = (LocalDateTime) object;
            out.writeString(DateTimeUtil.toDateTimeStr(localDateTime));
        }
    }
}
