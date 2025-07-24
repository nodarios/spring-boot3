package com.example.config;

import com.example.entity.MyEntity;
import com.example.entity.MyEntity2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableBatchProcessing
@EnableAsync
@RequiredArgsConstructor
@Slf4j
public class MyBatchConfig {

    private final JobRepository jobRepository;

    private static final int THREAD_SIZE = 4;
    private static final int PARTITION_SIZE = 4;
    private static final int CHUNK_SIZE = 1;
    private static final int PAGE_SIZE = 1;

    @Bean
    public Job myJob(Step myMasterStep) {
        return new JobBuilder("myJob", jobRepository)
                .start(myMasterStep)
                .build();
    }

    // masterStep creates partitions using the provided Partitioner
    // each partition receives a distinct ExecutionContext (i.e. its own data subset)
    // and each partition gets its own instance of ItemReader, ItemProcessor and ItemWriter (due to @StepScope)
    @Bean
    public Step myMasterStep(Partitioner myPartitioner,
                             TaskExecutor myTaskExecutor,
                             Step mySlaveStep) {
        return new StepBuilder("myMasterStep", jobRepository)
                .partitioner("mySlaveStep", myPartitioner)
                .gridSize(PARTITION_SIZE)
                .step(mySlaveStep)
                .taskExecutor(myTaskExecutor)
                .build();
    }

    @Bean
    public Step mySlaveStep(ItemReader<MyEntity> myItemReader,
                            ItemProcessor<MyEntity, MyEntity2> myItemProcessor,
                            ItemWriter<MyEntity2> myItemWriter,
                            PlatformTransactionManager transactionManager) {
        return new StepBuilder("mySlaveStep", jobRepository)
                .<MyEntity, MyEntity2>chunk(CHUNK_SIZE, transactionManager)
                .reader(myItemReader)
                .processor(myItemProcessor)
                .writer(myItemWriter)
                .build();
    }

    @Bean
    public Partitioner myPartitioner() {
        return gridSize -> {
            Map<String, ExecutionContext> partitions = new HashMap<>();
            for (int i = 0; i < gridSize; i++) {
                ExecutionContext context = new ExecutionContext();
                context.putInt("partitionIndex", i);
                context.putInt("gridSize", gridSize);
                context.putInt("pageSize", PAGE_SIZE);
                partitions.put("partition" + i, context);
            }
            return partitions;
        };
    }

    @Bean
    public TaskExecutor myTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(THREAD_SIZE);
        executor.setMaxPoolSize(THREAD_SIZE);
        executor.setThreadNamePrefix("batch-thread-");
        executor.setWaitForTasksToCompleteOnShutdown(true); // Graceful shutdown
        executor.initialize();
        return executor;
    }

}
