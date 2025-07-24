package com.example.service;

import com.example.entity.MyEntity;
import com.example.repo.MyRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MyService {

    private final MyRepo myRepo;

    @Transactional
    public void save(MyEntity myEntity) {
        myRepo.save(myEntity);
    }

    @Transactional
    public void saveAll(List<MyEntity> myEntityList) {
        myRepo.saveAll(myEntityList);
    }

    @Transactional(readOnly = true)
    public MyEntity get(UUID id) {
        return myRepo.findById(id).orElseThrow();
    }

    @Transactional(readOnly = true)
    public List<MyEntity> getAll(Sort sort) {
        return myRepo.findAll(sort);
    }

    @Transactional(readOnly = true)
    public Page<MyEntity> getAll(Pageable pageable) {
        return myRepo.findAll(pageable);
    }

}
