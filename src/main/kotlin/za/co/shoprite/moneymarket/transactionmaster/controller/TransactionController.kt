package za.co.shoprite.moneymarket.transactionmaster.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.*
import za.co.shoprite.moneymarket.transactionmaster.dto.TransactionRequestDto
import za.co.shoprite.moneymarket.transactionmaster.model.TransactionInformation
import za.co.shoprite.moneymarket.transactionmaster.model.TransactionReportInformation
import za.co.shoprite.moneymarket.transactionmaster.model.enum.TransactionType
import za.co.shoprite.moneymarket.transactionmaster.service.ReportService
import za.co.shoprite.moneymarket.transactionmaster.service.TransactionService
import za.co.shoprite.moneymarket.transactionmaster.service.ValidationService

@RestController
class TransactionController {

    @Autowired
    lateinit var transactionService: TransactionService

    @Autowired
    lateinit var validationService: ValidationService

    @Autowired
    lateinit var reportService: ReportService

    @PostMapping("/transaction/master/deposit")
    @PreAuthorize("hasRole('deposit')")
    fun deposit(@RequestBody transactionRequestDto: TransactionRequestDto, @AuthenticationPrincipal principal: Jwt): ResponseEntity<Unit> {



        val transactionInformation: TransactionInformation =
            validationService.validateAndBuildTransactionInformation(
                principal.getClaimAsString("preferred_username"), TransactionType.DEPOSIT, transactionRequestDto
            )
        return ResponseEntity.ok(transactionService.processTransaction(transactionInformation))
    }

    @PostMapping("/transaction/master/transfer")
    @PreAuthorize("hasRole('transfer')")
    fun transfer(@RequestBody transactionRequestDto: TransactionRequestDto, @AuthenticationPrincipal principal: Jwt): ResponseEntity<Unit> {

        val transactionInformation: TransactionInformation =
            validationService.validateAndBuildTransactionInformation(
                principal.getClaimAsString("preferred_username"), TransactionType.TRANSFER, transactionRequestDto
            )
        return ResponseEntity.ok(transactionService.processTransaction(transactionInformation))
    }

    @GetMapping("/transaction/master/report")
    @PreAuthorize("hasRole('transfer')")
    fun report(@AuthenticationPrincipal principal: Jwt): ResponseEntity<TransactionReportInformation> {
        val tranactionReportInformation = transactionService.retrieveUserTransactions(principal.getClaimAsString("preferred_username"))
        reportService.generateTransactionReport(tranactionReportInformation)
        return ResponseEntity.ok(tranactionReportInformation)
    }

}