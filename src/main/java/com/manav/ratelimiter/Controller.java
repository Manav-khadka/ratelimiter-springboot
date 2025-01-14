package com.manav.ratelimiter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @WithRateLimitProtection
    @GetMapping("/")
    public String hello() {
        return "Hello, World!";
    }
}
