package com.iteo.priceservice.controllers

import com.iteo.priceservice.WebMvcSpecIT
import org.springframework.http.MediaType

import static com.iteo.priceservice.config.ApplicationConstants.API_PREFIX
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class PriceControllerSpecIT extends WebMvcSpecIT {

    def "CalculateProductsPrice"() {
        when:
        def result = mvc.perform(post(API_PREFIX + "/price")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    [
                        {
                            "quanity": 1
                        }
                    ]"""))
        then:
        result.andExpect(status().isBadRequest())
    }

}
