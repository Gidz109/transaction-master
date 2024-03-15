package za.co.shoprite.moneymarket.transactionmaster.controller

import io.micrometer.observation.annotation.Observed
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import za.co.shoprite.moneymarket.transactionmaster.dto.LoginRequestDto
import za.co.shoprite.moneymarket.transactionmaster.dto.LoginResponseDto
import za.co.shoprite.moneymarket.transactionmaster.service.AuthenticationService
import java.util.*

@RestController
class AuthenticationController {

    @Autowired
    lateinit var authenticationService: AuthenticationService

    private val logger = LoggerFactory.getLogger(AuthenticationController::class.java)

    @Observed
    @PostMapping("/transaction/authenticate")
    fun authenticate(@RequestBody loginRequestDto: LoginRequestDto): ResponseEntity<LoginResponseDto> {
        val requestId = UUID.randomUUID().toString()
        MDC.put("requestId", requestId)
        logger.info("RequestID - $requestId | Received request to authenticate user ${loginRequestDto.username}")
        val loginResponse = authenticationService.authenticate(loginRequestDto.username, loginRequestDto.password)
        MDC.clear()
        return ResponseEntity.ok(LoginResponseDto(loginResponse))
    }

}