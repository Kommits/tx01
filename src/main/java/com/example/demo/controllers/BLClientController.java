package com.example.demo.controllers;

import com.example.demo.services.BLClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BLClientController {
    private final BLClientService clientService;

    //
    // согласно требованиям добавляю контроллер GET
    @GetMapping(value = "/go")
    public String goBLClientService(){
        clientService.fromResourceFileToKafka();
        return ":: command has executed";
    }
}
