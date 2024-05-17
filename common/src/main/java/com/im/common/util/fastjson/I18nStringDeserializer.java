package com.im.common.util.fastjson;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.im.common.util.i18n.I18nTranslateUtil;
import com.im.common.util.mybatis.typehandler.i18n.I18nString;

import java.lang.reflect.Type;

/**
 * 国际化字段反序列化
 *
 * @author Barry
 * @date 2019-10-22
 */
public class I18nStringDeserializer implements ObjectDeserializer {
    public static final I18nStringDeserializer INSTANCE = new I18nStringDeserializer();

    @Override
    public I18nString deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        String val = parser.lexer.stringVal();
        return new I18nString(I18nTranslateUtil.translate(val));
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}
