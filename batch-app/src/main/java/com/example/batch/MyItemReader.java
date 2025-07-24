package com.example.batch;

import com.example.entity.MyEntity;
import com.example.service.MyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@StepScope
@Slf4j
public class MyItemReader implements ItemReader<MyEntity> {

    private final MyService myService;
    private final int pageSize;
    private final int partitionIndex;
    private final int gridSize;

    private int currentPage = 0;
    private List<MyEntity> currentBatch = new ArrayList<>();
    private int indexInBatch = 0;

    public MyItemReader(MyService myService,
                        @Value("#{stepExecutionContext['pageSize']}") int pageSize,
                        @Value("#{stepExecutionContext['partitionIndex']}") int partitionIndex,
                        @Value("#{stepExecutionContext['gridSize']}") int gridSize) {
        this.myService = myService;
        this.pageSize = pageSize;
        this.partitionIndex = partitionIndex;
        this.gridSize = gridSize;
    }

    @Override
    public MyEntity read() {
        if (indexInBatch >= currentBatch.size()) {
            int actualPage = partitionIndex + (currentPage * gridSize);
            Pageable pageable = PageRequest.of(actualPage, pageSize, Sort.by(Sort.Direction.ASC, "id"));
            Page<MyEntity> page = myService.getAll(pageable);
            currentBatch = page.getContent();
            indexInBatch = 0;
            currentPage++;
            if (currentBatch.isEmpty()) {
                log.info("No more data, batch job stops");
                return null;
            }
        }
        return currentBatch.get(indexInBatch++);
    }

}
