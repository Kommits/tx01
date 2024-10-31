package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TargetControllers {
    @PostMapping("/helloWorldA")
    String helloWorldA(){
        return "hello world A";
    }

    @GetMapping("/helloWorldB")
    String helloWorldB(){
        return "hello world B";
    }
}
