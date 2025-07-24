package com.example.controller;

import com.example.entity.MyEntity2;
import com.example.service.MyService2;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my-entity2")
public class MyController2 {

    private final MyService2 myService2;

    @GetMapping
    public List<MyEntity2> getAll() {
        return myService2.getAll(Sort.by(Sort.Direction.ASC, "id"));
    }

}
