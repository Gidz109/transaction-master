package za.co.shoprite.moneymarket.transactionmaster.controller

import io.micrometer.observation.annotation.Observed
import org.slf4j.LoggerFactory
import org.slf4j.MDC
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
import java.util.*

@RestController
class TransactionController {

    @Autowired
    lateinit var transactionService: TransactionService

    @Autowired
    lateinit var validationService: ValidationService

    @Autowired
    lateinit var reportService: ReportService

    private val logger = LoggerFactory.getLogger(TransactionController::class.java)

    @Observed
    @PostMapping("/transaction/master/deposit")
    @PreAuthorize("hasRole('deposit')")
    fun deposit(@RequestBody transactionRequestDto: TransactionRequestDto, @AuthenticationPrincipal principal: Jwt): ResponseEntity<Unit> {
        val requestId = UUID.randomUUID().toString()
        MDC.put("requestId", requestId)
        logger.info("RequestID - $requestId | Received request to deposit funds for user ${principal.getClaimAsString("preferred_username")}")
        val transactionInformation: TransactionInformation =
            validationService.validateAndBuildTransactionInformation(
                principal.getClaimAsString("preferred_username"), TransactionType.DEPOSIT, transactionRequestDto
            )

        transactionService.processTransaction(transactionInformation)
        MDC.clear()
        return ResponseEntity.ok().build()
    }

    @Observed
    @PostMapping("/transaction/master/transfer")
    @PreAuthorize("hasRole('transfer')")
    fun transfer(@RequestBody transactionRequestDto: TransactionRequestDto, @AuthenticationPrincipal principal: Jwt): ResponseEntity<Unit> {
        val requestId = UUID.randomUUID().toString()
        MDC.put("requestId", requestId)
        logger.info("RequestID - $requestId | Received request to transfer funds for user ${principal.getClaimAsString("preferred_username")}")
        val transactionInformation: TransactionInformation =
            validationService.validateAndBuildTransactionInformation(
                principal.getClaimAsString("preferred_username"), TransactionType.TRANSFER, transactionRequestDto
            )
        transactionService.processTransaction(transactionInformation)
        MDC.clear()
        return ResponseEntity.ok().build()
    }

    @Observed
    /*This request should actually drop a message on a queue for an MDB to pick up and process on a seperate thread or by using spring batch.*/
    @GetMapping("/transaction/master/report")
    @PreAuthorize("hasRole('report')")
    fun report(@AuthenticationPrincipal principal: Jwt): ResponseEntity<TransactionReportInformation> {
        val tranactionReportInformation = transactionService.retrieveUserTransactions(principal.getClaimAsString("preferred_username"))
        reportService.generateTransactionReport(tranactionReportInformation)
        return ResponseEntity.ok(tranactionReportInformation)
    }

}