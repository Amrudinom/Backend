package com.Foerderportal.Backend.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/api/public")
public class TestController {

    @GetMapping
    public String test() {
        return "Backend läuft";
    }
}
