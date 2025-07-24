package com.example.batch;

import com.example.entity.MyEntity2;
import com.example.repo.MyRepo2;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@StepScope
@RequiredArgsConstructor
public class MyItemWriter implements ItemWriter<MyEntity2> {

    private final MyRepo2 myRepo2;

    @Override
    public void write(Chunk<? extends MyEntity2> items) {
        myRepo2.saveAll(items.getItems());
    }

}
