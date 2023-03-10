package com.iteo.priceservice.controllers;

import com.iteo.priceservice.config.ApplicationConstants;
import com.iteo.priceservice.models.ProductRequest;
import com.iteo.priceservice.models.ProductResponse;
import com.iteo.priceservice.services.PriceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApplicationConstants.API_PREFIX + "/price")
@Validated
@RequiredArgsConstructor
public class PriceController {

    private final PriceService service;

    @PostMapping
    public ResponseEntity<List<ProductResponse>> calculateProductsPrice(@Valid @RequestBody List<ProductRequest> products) {
        return ResponseEntity.ok(service.calculateProductsPrice(products));
    }

}
