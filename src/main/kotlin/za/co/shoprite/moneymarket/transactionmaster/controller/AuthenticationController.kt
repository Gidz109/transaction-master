package za.co.shoprite.moneymarket.transactionmaster.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import za.co.shoprite.moneymarket.transactionmaster.dto.LoginRequest
import za.co.shoprite.moneymarket.transactionmaster.dto.LoginResponse
import za.co.shoprite.moneymarket.transactionmaster.service.AuthenticationService

@RestController
class AuthenticationController {

    @Autowired
    lateinit var authenticationService: AuthenticationService

    @PostMapping("/transaction/authenticate")
    fun authenticate(@RequestBody loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        return ResponseEntity.ok(LoginResponse(authenticationService.authenticate(loginRequest.username, loginRequest.password)))
    }

}