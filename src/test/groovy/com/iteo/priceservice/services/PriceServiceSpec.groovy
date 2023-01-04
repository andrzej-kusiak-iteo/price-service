package com.iteo.priceservice.services

import com.iteo.priceservice.entities.Discount
import com.iteo.priceservice.entities.Product
import com.iteo.priceservice.models.ProductRequest
import com.iteo.priceservice.repositories.ProductRepository
import jakarta.persistence.EntityNotFoundException
import spock.lang.Specification
import spock.lang.Subject

class PriceServiceSpec extends Specification {

    ProductRepository productRepository = Stub()

    @Subject
    def service = new PriceService(productRepository)

    def "It should properly return product without discounts"() {
        given:
        def productId = UUID.fromString("1496f062-b1a9-4461-80bf-4daed33a5646")
        def productsRequest = [new ProductRequest(productId, 5)]

        productRepository.findById(productId) >> Optional.of(new Product(
                productId: productId,
                price: BigDecimal.valueOf(433, 2)
        ))

        when:
        def result = service.calculateProductsPrice(productsRequest)

        then:
        result.size() == 1
        def productResponse = result[0]
        productResponse.unitPrice == BigDecimal.valueOf(433, 2)
        productResponse.basePrice == BigDecimal.valueOf(2165, 2)
        productResponse.discountPrice == BigDecimal.valueOf(2165, 2)
    }

    def "It should properly return product with percentage discount"() {
        given:
        def productId = UUID.fromString("1496f062-b1a9-4461-80bf-4daed33a5646")
        def productsRequest = [new ProductRequest(productId, 5)]

        productRepository.findById(productId) >> Optional.of(new Product(
                productId: productId,
                price: BigDecimal.valueOf(511, 2),
                discounts: [new Discount(percentage: 33)]
        ))

        when:
        def result = service.calculateProductsPrice(productsRequest)

        then:
        result.size() == 1
        def productResponse = result[0]
        productResponse.unitPrice == BigDecimal.valueOf(511, 2)
        productResponse.basePrice == BigDecimal.valueOf(2555, 2)
        productResponse.discountPrice == BigDecimal.valueOf(1712, 2)
    }

    def "It should properly return product with amount based discount"() {
        given:
        def productId = UUID.fromString("1496f062-b1a9-4461-80bf-4daed33a5646")
        def productsRequest = [new ProductRequest(productId, 3)]

        productRepository.findById(productId) >> Optional.of(new Product(
                productId: productId,
                price: BigDecimal.valueOf(511, 2),
                discounts: [new Discount(amountFactor: BigDecimal.valueOf(133, 2))]
        ))

        when:
        def result = service.calculateProductsPrice(productsRequest)

        then:
        result.size() == 1
        def productResponse = result[0]
        productResponse.unitPrice == BigDecimal.valueOf(511, 2)
        productResponse.basePrice == BigDecimal.valueOf(1533, 2)
        productResponse.discountPrice == BigDecimal.valueOf(1134, 2)
    }

    def "It should properly return product with amount and percentage based discount"() {
        given:
        def productId = UUID.fromString("1496f062-b1a9-4461-80bf-4daed33a5646")
        def productsRequest = [new ProductRequest(productId, 3)]

        productRepository.findById(productId) >> Optional.of(new Product(
                productId: productId,
                price: BigDecimal.valueOf(1544, 2),
                discounts: [
                        new Discount(amountFactor: BigDecimal.valueOf(177, 2)),
                        new Discount(percentage: 33)
                ]
        ))

        when:
        def result = service.calculateProductsPrice(productsRequest)

        then:
        result.size() == 1
        def productResponse = result[0]
        productResponse.unitPrice == BigDecimal.valueOf(1544, 2)
        productResponse.basePrice == BigDecimal.valueOf(4632, 2)
        productResponse.discountPrice == BigDecimal.valueOf(2572, 2)
    }

    def "It should properly round up price with overflow"() {
        given:
        def productId = UUID.fromString("1496f062-b1a9-4461-80bf-4daed33a5646")
        def productsRequest = [new ProductRequest(productId, 1)]

        productRepository.findById(productId) >> Optional.of(new Product(
                productId: productId,
                price: BigDecimal.valueOf(1010, 2),
                discounts: [new Discount(percentage: 1)]
        ))

        when:
        def result = service.calculateProductsPrice(productsRequest)

        then:
        result.size() == 1
        def productResponse = result[0]
        productResponse.unitPrice == BigDecimal.valueOf(1010, 2)
        productResponse.basePrice == BigDecimal.valueOf(1010, 2)
        and: "9.999 rounded to 10"
        productResponse.discountPrice == BigDecimal.valueOf(1000, 2)
    }

    def "It should properly throw exception for not found product"() {
        given:
        def productId = UUID.fromString("1496f062-b1a9-4461-80bf-4daed33a5646")
        def productsRequest = [new ProductRequest(productId, 5)]

        productRepository.findById(productId) >> Optional.empty()

        when:
        service.calculateProductsPrice(productsRequest)

        then:
        thrown(EntityNotFoundException.class)
    }

    def "It should properly throw exception for not positive quantity"() {
        given:
        def productId = UUID.fromString("1496f062-b1a9-4461-80bf-4daed33a5646")
        def productsRequest = [new ProductRequest(productId, quantity)]

        productRepository.findById(productId) >> Optional.empty()

        when:
        service.calculateProductsPrice(productsRequest)

        then:
        thrown(IllegalArgumentException.class)

        where:
        quantity | _
        -100     | _
        -1       | _
        0        | _
    }

}
