package com.iteo.priceservice.services;

import com.iteo.priceservice.entities.Product;
import com.iteo.priceservice.models.ProductRequest;
import com.iteo.priceservice.models.ProductResponse;
import com.iteo.priceservice.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public record PriceService(
        ProductRepository repository
) {
    public List<ProductResponse> calculateProductsPrice(List<ProductRequest> products) {
        return null;
    }
}
