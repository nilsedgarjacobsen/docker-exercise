package com.nilsedgar.dockerexercise;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    @GetMapping("/hello")
    public String hello(){
        return "<h1>Hello!</h1>";
    }

}
