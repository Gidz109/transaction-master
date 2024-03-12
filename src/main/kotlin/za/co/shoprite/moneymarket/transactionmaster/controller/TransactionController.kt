package za.co.shoprite.moneymarket.transactionmaster.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import za.co.shoprite.moneymarket.transactionmaster.dto.DepositRequest
import za.co.shoprite.moneymarket.transactionmaster.dto.LoginRequest
import za.co.shoprite.moneymarket.transactionmaster.dto.LoginResponse
import za.co.shoprite.moneymarket.transactionmaster.service.AuthenticationService


@RestController
class TransactionController {

    @PostMapping("/transaction/master/deposit")
    fun deposit(@RequestBody depositRequest: DepositRequest): String {
        return "Success";
    }

}