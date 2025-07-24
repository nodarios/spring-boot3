package com.example.service;

import com.example.entity.MyEntity2;
import com.example.repo.MyRepo2;
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
public class MyService2 {

    private final MyRepo2 myRepo2;

    @Transactional(readOnly = true)
    public List<MyEntity2> getAll(Sort sort) {
        return myRepo2.findAll(sort);
    }

}
