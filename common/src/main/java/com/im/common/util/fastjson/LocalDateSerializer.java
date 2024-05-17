package com.im.common.util.fastjson;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.im.common.util.DateTimeUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;

/**
 * 日期序列化
 *
 * @author Barry
 * @date 2019-10-12
 */
public class LocalDateSerializer implements ObjectSerializer {
    public static final LocalDateSerializer INSTANCE = new LocalDateSerializer();

    public LocalDateSerializer() {
    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull();
        } else {
            LocalDate localDate = (LocalDate) object;
            out.writeString(DateTimeUtil.toDateStr(localDate));
        }
    }
}
