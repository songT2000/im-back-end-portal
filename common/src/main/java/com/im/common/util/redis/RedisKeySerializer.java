package com.im.common.util.redis;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.util.Assert;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Redis Key序列化，补充{@link org.springframework.data.redis.serializer.StringRedisSerializer}无法转换Object为String
 *
 * @author Barry
 * @date 2019/2/8
 */
public class RedisKeySerializer implements RedisSerializer<Object> {
    private final Charset charset;

    /**
     * Creates a new {@link RedisKeySerializer} using {@link StandardCharsets#UTF_8 UTF-8}.
     */
    public RedisKeySerializer() {
        this(StandardCharsets.UTF_8);
    }

    /**
     * Creates a new {@link RedisKeySerializer} using the given {@link Charset} to encode and decode strings.
     *
     * @param charset must not be {@literal null}.
     */
    public RedisKeySerializer(Charset charset) {

        Assert.notNull(charset, "Charset must not be null!");
        this.charset = charset;
    }

    @Override
    public byte[] serialize(Object o) throws SerializationException {
        return (o == null ? null : o.toString().getBytes(charset));
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        return (bytes == null ? null : new String(bytes, charset));
    }
}
