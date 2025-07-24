package com.example.controller;

import com.example.batch.MyJobLauncher;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batch")
@RequiredArgsConstructor
public class MyJobController {

    private final MyJobLauncher myJobLauncher;

    @GetMapping("/run")
    public String runJob() {
        myJobLauncher.launchMyJobAsync();
        return "Batch job started";
    }

}
