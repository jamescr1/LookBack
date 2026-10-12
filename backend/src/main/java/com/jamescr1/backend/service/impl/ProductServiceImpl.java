package com.jamescr1.backend.service.impl;

import com.jamescr1.backend.entity.Product;
import com.jamescr1.backend.repository.ProductRepository;
import com.jamescr1.backend.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;

    @Override
    public List<Product> getProducts() {
        return productRepository.findAll();
    }
}
