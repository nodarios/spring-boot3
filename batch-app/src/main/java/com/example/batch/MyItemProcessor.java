package com.example.batch;

import com.example.entity.MyEntity;
import com.example.entity.MyEntity2;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class MyItemProcessor implements ItemProcessor<MyEntity, MyEntity2> {

    private final int partitionIndex;
    private final int gridSize;

    public MyItemProcessor(@Value("#{stepExecutionContext['partitionIndex']}") int partitionIndex,
                           @Value("#{stepExecutionContext['gridSize']}") int gridSize) {
        this.partitionIndex = partitionIndex;
        this.gridSize = gridSize;
    }

    @Override
    public MyEntity2 process(MyEntity myEntity) {
        String description =
                "partitionIndex: " + partitionIndex +
                        " gridSize: " + gridSize +
                        " threadName: " + Thread.currentThread().getName();
        return new MyEntity2(myEntity.getId(), description);
    }

}
