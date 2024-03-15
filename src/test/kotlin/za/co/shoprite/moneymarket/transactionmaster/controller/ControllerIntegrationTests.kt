package za.co.shoprite.moneymarket.transactionmaster.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.core.io.ResourceLoader
import org.springframework.http.*
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.client.RestTemplate
import za.co.shoprite.moneymarket.transactionmaster.config.TestConfig
import za.co.shoprite.moneymarket.transactionmaster.dto.LoginRequestDto
import za.co.shoprite.moneymarket.transactionmaster.dto.LoginResponseDto
import za.co.shoprite.moneymarket.transactionmaster.dto.TransactionRequestDto

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
@Import(TestConfig::class)
class ControllerIntegrationTests {

    @Autowired
    private lateinit var resourceLoader: ResourceLoader

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var restTemplate: RestTemplate

    @LocalServerPort
    private var port: Int = 0

    private var jwtDecoder: JwtDecoder? = null

    @Autowired
    fun JwtService(jwtDecoder: JwtDecoder?) {
        this.jwtDecoder = jwtDecoder
    }

    @Order(1)
    @Test
    fun testAuthentication_success()    {

        val authorizationRequest = objectMapper.readValue(
            resourceLoader.getResource("classpath:/requests/auth/authorizationRequest.json")
                .getFile(),
            LoginRequestDto::class.java
        )

        val responseEntity: ResponseEntity<LoginResponseDto> = restTemplate.postForEntity(
            "http://localhost:$port/transaction/authenticate",
            authorizationRequest,
            LoginResponseDto::class.java
        )

        SharedTestData.jwtToken = responseEntity.body!!.jwtToken

        val jwt: Jwt = jwtDecoder!!.decode(SharedTestData.jwtToken)

        Assertions.assertEquals(authorizationRequest.username, jwt.getClaim("preferred_username"))

    }

    @Order(2)
    @Test
    fun testDeposit_success()   {

        val depositRequest = objectMapper.readValue(
            resourceLoader.getResource("classpath:/requests/deposit/depositRequest.json")
                .getFile(),
            TransactionRequestDto::class.java
        )

        val headers = HttpHeaders()
        headers.setBearerAuth(SharedTestData.jwtToken!!)

        val request = HttpEntity(depositRequest, headers)

        val responseEntity = restTemplate.exchange(
            "http://localhost:$port/transaction/master/deposit",
            HttpMethod.POST,
            request,
            Void::class.java
        )

        Assertions.assertEquals(HttpStatus.OK.value(), responseEntity.statusCode.value())

    }

    @Order(3)
    @Test
    fun testTransfer_success()   {

        val depositRequest = objectMapper.readValue(
            resourceLoader.getResource("classpath:/requests/transfer/transferRequest.json")
                .getFile(),
            TransactionRequestDto::class.java
        )

        val headers = HttpHeaders()
        headers.setBearerAuth(SharedTestData.jwtToken!!)

        val request = HttpEntity(depositRequest, headers)

        val responseEntity = restTemplate.exchange(
            "http://localhost:$port/transaction/master/transfer",
            HttpMethod.POST,
            request,
            Void::class.java
        )

        Assertions.assertEquals(HttpStatus.OK.value(), responseEntity.statusCode.value())

    }

    object SharedTestData {
        var jwtToken: String? = null
    }

}

