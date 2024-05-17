package com.im;

import com.im.common.util.ApplicationUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Instant;

/**
 * 回调服务
 */
@MapperScan("com.im.common.mapper")
@SpringBootApplication
@EnableAspectJAutoProxy
@EnableAsync(proxyTargetClass = true)
public class CallbackApplication {
    public static void main(String[] args) {
        Instant start = Instant.now();

        SpringApplication.run(CallbackApplication.class);

        ApplicationUtil.printStartSuccess("回调服务", start);
    }
}
