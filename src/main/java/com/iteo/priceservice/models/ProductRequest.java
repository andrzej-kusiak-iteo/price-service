package com.iteo.priceservice.models;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProductRequest(

        @NotNull
        UUID productId,

        @NotNull
        Integer quantity
) {
}
