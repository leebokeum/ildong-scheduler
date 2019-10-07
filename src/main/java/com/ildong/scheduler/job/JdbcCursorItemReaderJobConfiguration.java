package com.ildong.scheduler.job;

import com.ildong.scheduler.domain.EtcMenuTable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;

@Slf4j
@RequiredArgsConstructor
//@Configuration
public class JdbcCursorItemReaderJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource; // DataSource DI

    private static final int chunkSize = 10;

    @Bean
    public Job jdbcCursorItemReaderJob() {
        return jobBuilderFactory.get("jdbcCursorItemReaderJob4")
                .start(jdbcCursorItemReaderStep())
                .build();
    }

    @Bean
    public Step jdbcCursorItemReaderStep() {
        return stepBuilderFactory.get("jdbcCursorItemReaderStep")
                .<EtcMenuTable, EtcMenuTable>chunk(chunkSize)
                // 첫번째 EtcMenuTable Reader에서 반환할 타입이며, 두번째 EtcMenuTable Writer에 파라미터로 넘어올 타입을 얘기
                //chunkSize로 인자값을 넣은 경우는 Reader & Writer가 묶일 Chunk 트랜잭션 범위입니다
                .reader(jdbcCursorItemReader())
                //.writer(jdbcCursorItemWriter())
                .build();
    }

    @Bean
    public JdbcCursorItemReader<EtcMenuTable> jdbcCursorItemReader() {
        return new JdbcCursorItemReaderBuilder<EtcMenuTable>()
                .fetchSize(chunkSize)
                //Database에서 한번에 가져올 데이터 양을 나타냅니다.
                .dataSource(dataSource)
                //Database에 접근하기 위해 사용할 Datasource 객체를 할당합니다
                .rowMapper(new BeanPropertyRowMapper<>(EtcMenuTable.class))
                //쿼리 결과를 Java 인스턴스로 매핑하기 위한 Mapper 입니다.
                //커스텀하게 생성해서 사용할 수 도 있지만, 이렇게 될 경우 매번 Mapper 클래스를 생성해야 되서 보편적으로는 Spring에서 공식적으로 지원하는 BeanPropertyRowMapper.class를 많이 사용합니다
                .sql("SELECT id FROM Etc_Menu_Table")
                //Reader로 사용할 쿼리문을 사용하시면 됩니다.
                .name("jdbcCursorItemReader")
                //reader의 이름을 지정합니다.
                //Bean의 이름이 아니며 Spring Batch의 ExecutionContext에서 저장되어질 이름입니다.
                .build();
    }

    private ItemWriter<EtcMenuTable> jdbcCursorItemWriter() {
        return list -> {
            for (EtcMenuTable pay: list) {
                log.info("Current Pay={}", pay);
            }
        };
    }
}
