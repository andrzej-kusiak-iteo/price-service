package com.iteo.priceservice


import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class PriceServiceApplicationSpec extends Specification {

    def "Load context"() {
        expect:
        1 == 1
    }

}
