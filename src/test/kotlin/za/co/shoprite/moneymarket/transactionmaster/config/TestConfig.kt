package za.co.shoprite.moneymarket.transactionmaster.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.web.client.RestTemplate


@TestConfiguration
class TestConfig {
    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }

    @Bean
    fun jwtDecoder(): JwtDecoder {
        // Replace "your-jwt-signing-key" with your actual JWT signing key
        return NimbusJwtDecoder.withJwkSetUri("http://localhost:8080/realms/payments-realm/protocol/openid-connect/certs")
            .jwsAlgorithm(SignatureAlgorithm.RS256)
            .build()
    }
}