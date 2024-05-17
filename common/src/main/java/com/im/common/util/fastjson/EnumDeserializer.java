package com.im.common.util.fastjson;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.im.common.entity.enums.BaseEnum;
import com.im.common.util.EnumUtil;

import java.lang.reflect.Type;

/**
 * 枚举反序列化
 *
 * @author Barry
 * @date 2019-11-13
 */
public class EnumDeserializer implements ObjectDeserializer {
    public static final EnumDeserializer INSTANCE = new EnumDeserializer();

    @Override
    public BaseEnum deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        try {
            Class<? extends BaseEnum> clazz = (Class<? extends BaseEnum>) Class.forName(type.getTypeName());

            String val = parser.lexer.stringVal();
            BaseEnum iEnum = EnumUtil.valueOfIEnum(clazz, val);
            return iEnum;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}
