package com.iteo.priceservice.services;

import com.iteo.priceservice.entities.Discount;
import com.iteo.priceservice.entities.Product;
import com.iteo.priceservice.models.ProductRequest;
import com.iteo.priceservice.models.ProductResponse;
import com.iteo.priceservice.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public record PriceService(
        ProductRepository repository
) {

    public List<ProductResponse> calculateProductsPrice(List<ProductRequest> products) {
        return products.stream()
                .map(this::calculateProductPrice)
                .toList();
    }

    private ProductResponse calculateProductPrice(ProductRequest productRequest) {
        Product product = repository.findById(productRequest.productId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Product with id: %s not found", productRequest.productId())));

        BigDecimal basePrice = product.getPrice().multiply(BigDecimal.valueOf(productRequest.quantity()));
        return ProductResponse.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .quantity(productRequest.quantity())
                .unitPrice(product.getPrice())
                .basePrice(basePrice)
                .discountPrice(calculateDiscountPrice(product, productRequest.quantity(), basePrice))
                .build();
    }

    private BigDecimal calculateDiscountPrice(Product product, Integer quantity, BigDecimal basePrice) {
        return product.getDiscounts().stream()
                .map(discount -> calculateDiscount(discount, quantity, basePrice))
                .reduce(basePrice, BigDecimal::subtract);
    }

    private BigDecimal calculateDiscount(Discount discount, Integer quantity, BigDecimal basePrice) {
        return discount.getPercentage() != null ?
                calculatePercentageBasedDiscount(basePrice, discount.getPercentage())
                : calculateAmountBasedDiscount(quantity, discount.getAmountFactor());
    }

    private static BigDecimal calculatePercentageBasedDiscount(BigDecimal basePrice, Integer percentage) {
        return basePrice.multiply(BigDecimal.valueOf(percentage));
    }

    private static BigDecimal calculateAmountBasedDiscount(Integer quantity, BigDecimal factor) {
        return BigDecimal.valueOf(quantity).multiply(factor);
    }

}
