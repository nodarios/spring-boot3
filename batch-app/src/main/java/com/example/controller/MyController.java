package com.example.controller;

import com.example.entity.MyEntity;
import com.example.service.MyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my-entity")
public class MyController {

    private final MyService myService;

    @PostMapping
    public void save(@RequestBody MyEntity myEntity) {
        myService.save(myEntity);
    }

    @PostMapping("/bulk")
    public void saveAll(@RequestBody List<MyEntity> myEntityList) {
        myService.saveAll(myEntityList);
    }

    @GetMapping("/{id}")
    public MyEntity get(@PathVariable UUID id) {
        return myService.get(id);
    }

    @GetMapping
    public List<MyEntity> getAll() {
        return myService.getAll(Sort.by(Sort.Direction.ASC, "id"));
    }

}
