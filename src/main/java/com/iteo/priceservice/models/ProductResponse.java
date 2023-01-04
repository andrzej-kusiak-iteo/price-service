package com.iteo.priceservice.models;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record ProductResponse(
        UUID productId,
        String name,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal basePrice,
        BigDecimal discountPrice
) {
}
