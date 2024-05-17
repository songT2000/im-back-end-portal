package com.im;

import com.im.common.util.ApplicationUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

import java.time.Instant;

/**
 * 运行该类启动前端，支持多节点运行
 *
 * @author Barry
 * @date 2020-05-23
 */
@ServletComponentScan
@MapperScan("com.im.common.mapper")
@SpringBootApplication
@EnableAspectJAutoProxy
@EnableAsync(proxyTargetClass = true)
public class PortalApplication {

    public static void main(String[] args) {
        Instant start = Instant.now();

        SpringApplication.run(PortalApplication.class, args);

        ApplicationUtil.printStartSuccess("前台服务", start);
    }
}
