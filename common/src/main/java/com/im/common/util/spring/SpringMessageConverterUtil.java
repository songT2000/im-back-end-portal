package com.im.common.util.spring;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.im.common.util.fastjson.FastJsonConfigUtil;
import org.springframework.http.converter.StringHttpMessageConverter;

import java.nio.charset.Charset;

/**
 * 配置Spring JSON处理组件为FastJson
 *
 * @author Barry
 * @date 2018/10/5
 */
public final class SpringMessageConverterUtil {
    private SpringMessageConverterUtil() {
    }

    /**
     * 普通的String类型的转换器
     *
     * @return
     */
    public static StringHttpMessageConverter buildStringConverter() {
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        return stringHttpMessageConverter;
    }

    /**
     * json 类型转换器
     *
     * @return
     */
    public static FastJsonHttpMessageConverter buildFastJsonConverter() {
        return FastJsonConfigUtil.buildFastJsonConverter();
    }
}
