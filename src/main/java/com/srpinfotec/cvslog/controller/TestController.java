package com.srpinfotec.cvslog.controller;

import com.srpinfotec.cvslog.domain.TestEntity;
import com.srpinfotec.cvslog.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final TestRepository testRepository;

    @GetMapping("/test")
    public String test(){
        return "Hello World";
    }

    @GetMapping("/test/save")
    public String testEntitySave(){
        testRepository.save(new TestEntity("name1"));
        return "Done";
    }
}
