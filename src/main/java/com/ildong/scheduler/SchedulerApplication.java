package com.ildong.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableBatchProcessing
@Slf4j
//@PropertySource("classpath:application.properties")
public class SchedulerApplication implements ApplicationRunner {
    @Value("${spring.profiles.active}")
    private String profiles;
    public static void main(String[] args) {
        SpringApplication.run(SchedulerApplication.class, args);
    }

    //default 시작 프로그램 설정
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info(profiles);
    }
}
