package com.im;

import com.im.common.util.ApplicationUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Instant;

/**
 * 调度服务启动类，仅能单节点运行
 *
 * @author Barry
 * @date 2019/10/18
 */
@MapperScan("com.im.common.mapper")
@SpringBootApplication
@EnableScheduling
@EnableAsync(proxyTargetClass = true)
public class SchedulerApplication {
    public static void main(String[] args) {
        Instant start = Instant.now();

        SpringApplication app = new SpringApplication(SchedulerApplication.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);

        ApplicationUtil.printStartSuccess("调度服务", start);
    }
}
