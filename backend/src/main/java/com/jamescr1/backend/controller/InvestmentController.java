package com.jamescr1.backend.controller;

import com.jamescr1.backend.entity.Product;
import com.jamescr1.backend.repository.ProductRepository;
import com.jamescr1.backend.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/investments")
@RequiredArgsConstructor
public class InvestmentController {

    private final IProductService iProductService;

    @GetMapping
    public List<Product> getInvestments() {
        List<Product> productList = iProductService.getProducts();
        return productList;
    }
}
