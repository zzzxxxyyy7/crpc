package com.crpc.springbootconsumer.controller;

import com.crpc.springbootconsumer.service.DemoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @Resource
    private DemoService demoService;

    @RequestMapping("/health")
    public String health() {
        demoService.userTest();
        return "ok";
    }

}
