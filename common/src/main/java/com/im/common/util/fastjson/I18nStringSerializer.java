package com.im.common.util.fastjson;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.im.common.util.i18n.I18nTranslateUtil;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 国际化字段序列化
 *
 * @author Barry
 * @date 2019-10-22
 */
public class I18nStringSerializer extends ToStringSerializer {
    public static final I18nStringSerializer INSTANCE = new I18nStringSerializer();

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
                      int features) throws IOException {
        SerializeWriter out = serializer.out;

        if (object == null) {
            out.writeNull();
            return;
        }

        String strVal = object.toString();
        out.writeString(I18nTranslateUtil.translate(strVal));
    }
}
