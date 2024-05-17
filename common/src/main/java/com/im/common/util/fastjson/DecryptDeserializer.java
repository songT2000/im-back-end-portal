package com.im.common.util.fastjson;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.im.common.util.MessageEncryptUtil;
import com.im.common.util.i18n.I18nTranslateUtil;
import com.im.common.util.mybatis.typehandler.i18n.I18nString;

import java.lang.reflect.Type;

/**
 * 解密字段反序列化
 */
public class DecryptDeserializer implements ObjectDeserializer {
    public static final DecryptDeserializer INSTANCE = new DecryptDeserializer();

    @Override
    public String deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        String val = parser.lexer.stringVal();
        return MessageEncryptUtil.decrypt(val);
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}
