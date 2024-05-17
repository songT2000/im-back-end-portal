package com.im.common.util.spring;

import cn.hutool.core.util.StrUtil;
import com.im.common.entity.enums.BaseEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * 配置Spring接收参数自动转换Enum
 *
 * @author Barry
 * @date 2018/5/12
 */
@Component
public class EnumConverterFactory implements ConverterFactory<String, BaseEnum> {
    /**
     * 转换失败时抛出异常的提示
     **/
    private static String ERROR_MSG = "field [{}]=[{}] is illegal";

    private static final Map<Class, Converter> CONVERTER_MAP = new WeakHashMap<>();

    @Override
    public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> targetType) {
        Converter result = CONVERTER_MAP.get(targetType);
        if (result == null) {
            result = new IntegerStrToEnum<>(targetType);
            CONVERTER_MAP.put(targetType, result);
        }
        return result;
    }

    static class IntegerStrToEnum<T extends BaseEnum> implements Converter<String, T> {
        private final Class<T> enumType;
        private Map<String, T> enumMap = new HashMap<>();

        public IntegerStrToEnum(Class<T> enumType) {
            this.enumType = enumType;
            T[] enums = enumType.getEnumConstants();
            for (T e : enums) {
                enumMap.put(e.getValue().toString(), e);
            }
        }

        @Override
        public T convert(String source) {
            if (StrUtil.isBlank(source)) {
                return null;
            }

            T result = enumMap.get(source);
            if (result == null) {
                // 抛出异常，拿不到参数名，只能用类型提示
                String msg = StrUtil.format(ERROR_MSG, enumType.getSimpleName(), source);
                throw new IllegalArgumentException(msg);
            }
            return result;
        }
    }
}
