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
 * 运行该类启动后台，支持多节点运行
 *
 * @author Barry
 * @date 2019-11-06
 */
@ServletComponentScan
@MapperScan("com.im.common.mapper")
@SpringBootApplication
@EnableAspectJAutoProxy
@EnableAsync(proxyTargetClass = true)
public class AdminApplication {

    public static void main(String[] args) {
        Instant start = Instant.now();

        SpringApplication.run(AdminApplication.class, args);

        ApplicationUtil.printStartSuccess("后台服务", start);
    }
}
