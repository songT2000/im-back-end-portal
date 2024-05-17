package com.im.common.util.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.im.common.entity.enums.BaseEnum;
import com.im.common.util.ClassUtil;
import com.im.common.util.CollectionUtil;
import com.im.common.util.api.im.tencent.entity.TiCustomItem;
import com.im.common.util.api.im.tencent.entity.param.message.TiMsgBody;
import com.im.common.util.mybatis.typehandler.encrypt.EncryptString;
import com.im.common.util.mybatis.typehandler.i18n.I18nString;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * fast json配置类
 * 1：配置默认的全局配置
 * 2：spring启动时也调用一下，以及时不生效
 *
 * @author Barry
 * @date 2019/2/15
 */
public final class FastJsonConfigUtil {
    public static int DEFAULT_GENERATE_FEATURE = JSON.DEFAULT_GENERATE_FEATURE;

    private FastJsonConfigUtil() {
    }

    static {
        configFastJson();
    }

    /**
     * 全局配置FastJson
     */
    public static void configFastJson() {
        buildSerializerFeature();

        buildSerializeConfig();

        buildDeserializeConfig();
    }

    public static SerializerFeature[] buildSerializerFeature() {
        // JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.WriteEnumUsingToString.getMask();
        // JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.BrowserCompatible.getMask();
        // JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.WriteDateUseDateFormat.getMask();
        // JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.WriteNullListAsEmpty.getMask();
        // JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.WriteNullBooleanAsFalse.getMask();
        // JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask();
        // JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.WriteMapNullValue.getMask();
        //
        // return new SerializerFeature[]{SerializerFeature.WriteEnumUsingToString,
        //         SerializerFeature.BrowserCompatible,
        //         SerializerFeature.WriteDateUseDateFormat,
        //         SerializerFeature.WriteNullListAsEmpty,
        //         SerializerFeature.WriteNullBooleanAsFalse,
        //         SerializerFeature.DisableCircularReferenceDetect,
        //         SerializerFeature.WriteMapNullValue};
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.WriteEnumUsingToString.getMask();
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.WriteDateUseDateFormat.getMask();
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.WriteNullListAsEmpty.getMask();
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.WriteNullBooleanAsFalse.getMask();
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask();
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.WriteMapNullValue.getMask();
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.WriteBigDecimalAsPlain.getMask();

        return new SerializerFeature[]{SerializerFeature.WriteEnumUsingToString,
                SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullBooleanAsFalse,
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteBigDecimalAsPlain};
    }

    /**
     * 配置序列化配置，同时全局设置FastJson
     *
     * @return
     */
    public static SerializeConfig buildSerializeConfig() {
        // 将大整型转换为String，因为如果数值过大，JS中会丢失精度，导致数据不正确，BrowserCompatible会根据值的大小来进行toString，这里是所有都toString
        SerializeConfig serializeConfig = SerializeConfig.globalInstance;
        serializeConfig.put(BigInteger.class, ToStringSerializer.instance);
        serializeConfig.put(Long.class, ToStringSerializer.instance);
        serializeConfig.put(Long.TYPE, ToStringSerializer.instance);

        // 自定义类型，如果有新增mybatis自定义类型，则需要在此定义
        serializeConfig.put(EncryptString.class, ToStringSerializer.instance);
        serializeConfig.put(I18nString.class, I18nStringSerializer.INSTANCE);

        // 日期时间
        serializeConfig.put(LocalDate.class, LocalDateSerializer.INSTANCE);
        serializeConfig.put(LocalDateTime.class, LocalDateTimeSerializer.INSTANCE);

        return serializeConfig;
    }

    public static ParserConfig buildDeserializeConfig() {
        ParserConfig globalInstance = ParserConfig.getGlobalInstance();

        // 获取所有BaseEnum的子类，注册到FastJson中

        String enumPackage = System.getProperty("mybatis-plus.type-enums-package");

        Set<Class<?>> classes = ClassUtil.scanPackageBySuper(enumPackage, BaseEnum.class);
        if (CollectionUtil.isNotEmpty(classes)) {
            for (Class<?> aClass : classes) {
                globalInstance.putDeserializer(aClass, EnumDeserializer.INSTANCE);
            }
        }

        globalInstance.putDeserializer(I18nString.class, I18nStringDeserializer.INSTANCE);

        globalInstance.putDeserializer(TiMsgBody.class,TiMsgBodyDeserializer.INSTANCE);
        globalInstance.putDeserializer(TiCustomItem.class,TiCustomItemDeserializer.INSTANCE);

        return globalInstance;
    }

    public static FastJsonConfig buildFastJsonConfig() {
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        // WriteEnumUsingToString：序列化枚举时调用toString
        // BrowserCompatible：目前只知道会将中文转码unicode，同时大整型(Long/BigInt等)会根据来进行toString以保证浏览器不会丢失精度
        fastJsonConfig.setSerializerFeatures(buildSerializerFeature());

        SerializeConfig serializeConfig = buildSerializeConfig();

        fastJsonConfig.setSerializeConfig(serializeConfig);

        ParserConfig parserConfig = buildDeserializeConfig();
        fastJsonConfig.setParserConfig(parserConfig);

        return fastJsonConfig;
    }

    /**
     * json 类型转换器
     *
     * @return
     */
    public static FastJsonHttpMessageConverter buildFastJsonConverter() {
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

        FastJsonConfig fastJsonConfig = buildFastJsonConfig();

        List<MediaType> fastMediaTypes = new ArrayList<>();

        MediaType jsonMediaType = MediaType.valueOf("application/json;charset=UTF-8");
        // 避免IE出现下载JSON文件的情况
        MediaType textMediaType = MediaType.valueOf("text/html;charset=UTF-8");

        fastMediaTypes.add(jsonMediaType);
        fastMediaTypes.add(textMediaType);

        fastConverter.setSupportedMediaTypes(fastMediaTypes);

        fastConverter.setFastJsonConfig(fastJsonConfig);

        return fastConverter;
    }
}