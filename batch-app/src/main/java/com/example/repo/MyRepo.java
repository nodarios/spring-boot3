package com.example.repo;

import com.example.entity.MyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MyRepo extends JpaRepository<MyEntity, UUID> {
}
