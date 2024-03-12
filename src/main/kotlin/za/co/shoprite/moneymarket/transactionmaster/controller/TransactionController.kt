package za.co.shoprite.moneymarket.transactionmaster.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController("/moneymarket/transaction/master")
class TransactionController {

    @GetMapping("/")
    fun index(@RequestParam("name") name: String) = "Hello, $name!"

}