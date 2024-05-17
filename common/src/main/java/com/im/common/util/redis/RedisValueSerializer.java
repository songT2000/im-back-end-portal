package com.im.common.util.redis;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.im.common.util.fastjson.FastJsonConfigUtil;

/**
 * Redis value序列化
 *
 * @author Barry
 * @date 2019/2/8
 */
public class RedisValueSerializer extends FastJsonRedisSerializer<Object> {

    public RedisValueSerializer() {
        super(Object.class);

        FastJsonConfig fastJsonConfig = FastJsonConfigUtil.buildFastJsonConfig();
        super.setFastJsonConfig(fastJsonConfig);
    }
}
