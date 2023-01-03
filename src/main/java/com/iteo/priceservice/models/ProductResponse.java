package com.iteo.priceservice.models;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse(
        UUID productId,
        Integer quantity,
        BigDecimal basePrice,
        BigDecimal discountPrice
) {
}
