package com.iteo.priceservice.controllers

import com.iteo.priceservice.WebMvcSpecIT
import com.iteo.priceservice.entities.Discount
import com.iteo.priceservice.entities.Product
import com.iteo.priceservice.repositories.DiscountRepository
import com.iteo.priceservice.repositories.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType

import static com.iteo.priceservice.config.ApplicationConstants.API_PREFIX
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class PriceControllerSpecIT extends WebMvcSpecIT {

    @Autowired
    ProductRepository productRepository

    @Autowired
    DiscountRepository discountRepository

    void cleanup() {
        productRepository.deleteAll()
        discountRepository.deleteAll()
    }

    def "It should properly calculate price for product"() {
        given:
        def product = productRepository.save(
                new Product(name: "Test1", price: BigDecimal.valueOf(299, 2)))
        def discount = discountRepository.save(
                new Discount(amountFactor: BigDecimal.valueOf(150, 2), product: product)
        )
        product.discounts << discount

        when:
        def result = mvc.perform(post(API_PREFIX + "/price")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    [
                        {
                            "productId": "$product.productId",
                            "quantity": 2
                        }
                    ]"""))

        then:
        result.andExpect(status().isOk())
                .andExpect(jsonPath("\$.length()").value(1))
                .andExpect(jsonPath("\$.[0].productId").value(product.productId.toString()))
                .andExpect(jsonPath("\$.[0].name").value("Test1"))
                .andExpect(jsonPath("\$.[0].quantity").value(2))
                .andExpect(jsonPath("\$.[0].unitPrice").value("2.99"))
                .andExpect(jsonPath("\$.[0].basePrice").value("5.98"))
                .andExpect(jsonPath("\$.[0].discountPrice").value("2.98"))
    }

    def "It should properly return not found for product that doesnt exist"() {
        given:
        def productId = "1496f062-b1a9-4461-80bf-4daed33a5646"

        when:
        def result = mvc.perform(post(API_PREFIX + "/price")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    [
                        {
                            "productId": "$productId",
                            "quantity": 1
                        }
                    ]"""))

        then:
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("\$.message").value("Product with id: $productId not found".toString()))
    }

    def "It should properly return bad request for missing quantity field"() {
        when:
        def result = mvc.perform(post(API_PREFIX + "/price")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    [
                        {
                            "productId": "1496f062-b1a9-4461-80bf-4daed33a5646"
                        }
                    ]"""))
        then:
        result.andExpect(status().isBadRequest())
            .andExpect(jsonPath("\$.[0].message").value("must not be null"))
            .andExpect(jsonPath("\$.[0].field").value("products[0].quantity"))
    }

    def "It should properly return bad request for quantity not positive"() {
        when:
        def result = mvc.perform(post(API_PREFIX + "/price")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    [
                        {
                            "productId": "1496f062-b1a9-4461-80bf-4daed33a5646",
                            "quantity": -1
                        }
                    ]"""))
        then:
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("\$.[0].message").value("must be greater than 0"))
                .andExpect(jsonPath("\$.[0].field").value("products[0].quantity"))
    }

}
