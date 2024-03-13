package za.co.shoprite.moneymarket.transactionmaster.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import za.co.shoprite.moneymarket.transactionmaster.model.TransactionInformation
import za.co.shoprite.moneymarket.transactionmaster.model.entity.AccountEntity
import za.co.shoprite.moneymarket.transactionmaster.model.entity.TransactionEntity
import za.co.shoprite.moneymarket.transactionmaster.model.enum.TransactionType
import za.co.shoprite.moneymarket.transactionmaster.repository.*
import za.co.shoprite.moneymarket.transactionmaster.service.TransactionService
import java.math.BigDecimal

@Service
class TransactionServiceImpl: TransactionService {

    @Autowired
    lateinit var transactionRepository: TransactionRepository

    @Autowired
    lateinit var transactionTypeRepository: TransactionTypeRepository

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Autowired
    lateinit var currencyRepository: CurrencyRepository

    @Autowired
    lateinit var exchangeRateRepository: ExchangeRateRepository

    override fun processTransaction(transactionInformation: TransactionInformation)    {

        var transactionEntity = TransactionEntity()
        val transactionCurrency = currencyRepository.findByCode(transactionInformation.currencyCode)

        transactionEntity.userId = transactionInformation.requestingUserId
        transactionEntity.transactionTypeId = transactionTypeRepository.findByCode(transactionInformation.transactionType.name).id
        transactionEntity.currencyId = transactionCurrency.id
        transactionEntity.controlSum = transactionInformation.controlSum

        var creditAmount: BigDecimal = transactionInformation.controlSum
        var exchangedAmount = calculateExchange(transactionCurrency.id!!, creditAmount)

        var creditAccount: AccountEntity = accountRepository.findById(transactionInformation.creditAccountId).get()
        transactionEntity.creditAccountId = creditAccount.id
        creditAccount(creditAccount, exchangedAmount);

        if(transactionInformation.transactionType == TransactionType.TRANSFER)  {
            var debitAccount: AccountEntity = accountRepository.findById(transactionInformation.debitAccountId!!).get()
            transactionEntity.debitAccountId = debitAccount.id
            debitAccount(debitAccount, exchangedAmount)
        }

        transactionRepository.save(transactionEntity)

    }

    fun creditAccount(accountEntity: AccountEntity, amount: BigDecimal) {
        accountEntity.balance = accountEntity.balance?.plus(amount)
        accountRepository.save(accountEntity)
    }

    fun debitAccount(accountEntity: AccountEntity, amount: BigDecimal)  {
        accountEntity.balance = accountEntity.balance?.minus(amount)
        accountRepository.save(accountEntity)
    }

    fun calculateExchange(currencyId: Short, creditAmount: BigDecimal): BigDecimal {
        val exchangeRate: BigDecimal = exchangeRateRepository.findByCurrencyId(currencyId).exchangeRate!!
        return creditAmount.times(exchangeRate)
    }


}