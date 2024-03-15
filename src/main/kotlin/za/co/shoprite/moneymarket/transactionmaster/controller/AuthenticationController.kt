package za.co.shoprite.moneymarket.transactionmaster.controller

import io.micrometer.observation.annotation.Observed
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import za.co.shoprite.moneymarket.transactionmaster.dto.LoginRequestDto
import za.co.shoprite.moneymarket.transactionmaster.dto.LoginResponseDto
import za.co.shoprite.moneymarket.transactionmaster.service.AuthenticationService

@RestController
class AuthenticationController {

    @Autowired
    lateinit var authenticationService: AuthenticationService

    @Observed
    @PostMapping("/transaction/authenticate")
    fun authenticate(@RequestBody loginRequestDto: LoginRequestDto): ResponseEntity<LoginResponseDto> {
        return ResponseEntity.ok(LoginResponseDto(authenticationService.authenticate(loginRequestDto.username, loginRequestDto.password)))
    }

}