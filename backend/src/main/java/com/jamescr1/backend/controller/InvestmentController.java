package com.jamescr1.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/investments")
public class InvestmentController {

    @GetMapping
    public String getInvestments() {
        return "Here is list of investments";
    }
}
