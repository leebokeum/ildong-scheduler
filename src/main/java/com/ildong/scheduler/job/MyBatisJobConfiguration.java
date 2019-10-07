package com.ildong.scheduler.job;

import com.ildong.scheduler.domain.EtcMenuTable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.mybatis.spring.batch.builder.MyBatisCursorItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class MyBatisJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private static final int chunkSize = 10;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Bean
    public Job myBatisCursorItemReaderJob() {
        return jobBuilderFactory.get("myBatisCursorItemReaderJob")
                .incrementer(new RunIdIncrementer())
                .flow(myBatisCursorItemReaderStep())
                .end()
                .build();
    }

    @Bean
    public Step myBatisCursorItemReaderStep() {
        return stepBuilderFactory.get("myBatisCursorItemReaderStep")
                .<EtcMenuTable, EtcMenuTable>chunk(chunkSize)
                // 첫번째 EtcMenuTable Reader에서 반환할 타입이며, 두번째 EtcMenuTable Writer에 파라미터로 넘어올 타입을 얘기
                //chunkSize로 인자값을 넣은 경우는 Reader & Writer가 묶일 Chunk 트랜잭션 범위입니다
                .reader(myBatisCursorItemReader())
                .writer(myBatisCursorItemWriter())
                .build();
    }

    @Bean
    public MyBatisCursorItemReader<EtcMenuTable> myBatisCursorItemReader() {
        return new MyBatisCursorItemReaderBuilder<EtcMenuTable>()
                .sqlSessionFactory(sqlSessionFactory)
                .queryId("test")
                .build();
    }

    private ItemWriter<EtcMenuTable> myBatisCursorItemWriter() {
        return list -> {
            for (EtcMenuTable pay: list) {
                log.info("Current Pay={}", pay);
            }
        };
    }
}
