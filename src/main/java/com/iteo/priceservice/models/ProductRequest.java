package com.iteo.priceservice.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record ProductRequest(

        @NotNull
        UUID productId,

        @NotNull
        @Positive
        Integer quantity
) {
}
