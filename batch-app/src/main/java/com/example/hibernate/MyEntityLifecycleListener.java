package com.example.hibernate;

import com.example.entity.MyEntity;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class MyEntityLifecycleListener implements PostInsertEventListener, PostUpdateEventListener {

    @Override
    public void onPostInsert(PostInsertEvent event) {
        if (event.getEntity() instanceof MyEntity myEntity) {
            UUID id = myEntity.getId();
            String description = myEntity.getDescription();
            CompletableFuture.runAsync(
                    () -> log.info("Insert: {} {}", id, description)
            );
        }
    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        if (event.getEntity() instanceof MyEntity myEntity) {
            UUID id = myEntity.getId();
            String description = myEntity.getDescription();
            CompletableFuture.runAsync(
                    () -> log.info("Update: {} {}", id, description)
            );
        }
    }

    @Override
    public boolean requiresPostCommitHandling(EntityPersister persister) {
        return true;
    }

}
