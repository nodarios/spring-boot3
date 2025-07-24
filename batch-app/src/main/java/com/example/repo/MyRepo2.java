package com.example.repo;

import com.example.entity.MyEntity2;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MyRepo2 extends JpaRepository<MyEntity2, UUID> {
}
