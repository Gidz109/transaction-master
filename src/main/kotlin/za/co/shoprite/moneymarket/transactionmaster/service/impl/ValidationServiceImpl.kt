package za.co.shoprite.moneymarket.transactionmaster.service.impl

import jakarta.persistence.EntityNotFoundException
import org.apache.coyote.BadRequestException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import za.co.shoprite.moneymarket.transactionmaster.dto.TransactionRequestDto
import za.co.shoprite.moneymarket.transactionmaster.model.TransactionInformation
import za.co.shoprite.moneymarket.transactionmaster.model.entity.AccountEntity
import za.co.shoprite.moneymarket.transactionmaster.model.entity.UserEntity
import za.co.shoprite.moneymarket.transactionmaster.model.enum.TransactionType
import za.co.shoprite.moneymarket.transactionmaster.repository.AccountRepository
import za.co.shoprite.moneymarket.transactionmaster.repository.AuthenticationRepository
import za.co.shoprite.moneymarket.transactionmaster.repository.CurrencyRepository
import za.co.shoprite.moneymarket.transactionmaster.repository.UserRepository
import za.co.shoprite.moneymarket.transactionmaster.service.ValidationService

@Service
class ValidationServiceImpl: ValidationService {

    @Autowired
    lateinit var authenticationRepository: AuthenticationRepository

    @Autowired
    lateinit var currencyRepository: CurrencyRepository

    @Autowired
    lateinit var accountRepository: AccountRepository

    override fun validateAndBuildTransactionInformation(username: String, transactionType: TransactionType, transactionRequest: TransactionRequestDto): TransactionInformation {

        var authenticationEntity = authenticationRepository.findByUsername(username)

        if(authenticationEntity == null)    {
            throw EntityNotFoundException("User not found")
        }

        var userId = authenticationEntity.userId

        var accountEntity: AccountEntity = accountRepository.findById(userId!!).get()

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
                    throw BadRequestException("User does not have sufficient funds for this transaction")
                }
            }
        }

        return TransactionInformation(transactionType, userId,
            debitTransactionId, creditTransactionId!!, transactionRequest.amount, transactionRequest.currency)

    }

}