package za.co.shoprite.moneymarket.transactionmaster.service.impl

import org.apache.coyote.BadRequestException
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import za.co.shoprite.moneymarket.transactionmaster.controller.TransactionController
import za.co.shoprite.moneymarket.transactionmaster.dto.TransactionRequestDto
import za.co.shoprite.moneymarket.transactionmaster.model.TransactionInformation
import za.co.shoprite.moneymarket.transactionmaster.model.entity.AccountEntity
import za.co.shoprite.moneymarket.transactionmaster.model.enum.TransactionType
import za.co.shoprite.moneymarket.transactionmaster.repository.AccountRepository
import za.co.shoprite.moneymarket.transactionmaster.repository.AuthenticationRepository
import za.co.shoprite.moneymarket.transactionmaster.service.ValidationService

@Service
class ValidationServiceImpl: ValidationService {

    @Autowired
    lateinit var authenticationRepository: AuthenticationRepository

    @Autowired
    lateinit var accountRepository: AccountRepository

    private val logger = LoggerFactory.getLogger(ValidationServiceImpl::class.java)

    override fun validateAndBuildTransactionInformation(username: String, transactionType: TransactionType, transactionRequest: TransactionRequestDto): TransactionInformation {

        logger.info("RequestID - ${MDC.get("requestId")} | validating account for user $username")

        val authenticationEntity = authenticationRepository.findByUsername(username)
        val userId = authenticationEntity.userId
        val accountEntity: AccountEntity = accountRepository.findById(userId!!).get()
        val creditTransactionId: Long?
        val debitTransactionId: Long?

        when(transactionType)   {
            TransactionType.DEPOSIT -> {
                creditTransactionId = accountEntity.id
                debitTransactionId = null
            }
            TransactionType.TRANSFER -> {
                debitTransactionId = accountEntity.id
                creditTransactionId = accountRepository.findByAccountNumber(transactionRequest.creditAccountNumber!!).id
                if(accountEntity.balance!! < transactionRequest.amount)    {
                    logger.error("RequestID - ${MDC.get("requestId")} | account validation failed")
                    throw BadRequestException("User does not have sufficient funds for this transaction")
                }
            }
        }

        logger.info("RequestID - ${MDC.get("requestId")} | account validation successful")

        return TransactionInformation(transactionType, userId,
            debitTransactionId, creditTransactionId!!, transactionRequest.amount, transactionRequest.currency)

    }

}