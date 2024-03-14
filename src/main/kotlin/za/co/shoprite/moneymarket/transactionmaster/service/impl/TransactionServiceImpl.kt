package za.co.shoprite.moneymarket.transactionmaster.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import za.co.shoprite.moneymarket.transactionmaster.model.TransactionInformation
import za.co.shoprite.moneymarket.transactionmaster.model.TransactionReportInformation
import za.co.shoprite.moneymarket.transactionmaster.model.TransactionSummary
import za.co.shoprite.moneymarket.transactionmaster.model.entity.AccountEntity
import za.co.shoprite.moneymarket.transactionmaster.model.entity.TransactionEntity
import za.co.shoprite.moneymarket.transactionmaster.model.enum.TransactionType
import za.co.shoprite.moneymarket.transactionmaster.repository.*
import za.co.shoprite.moneymarket.transactionmaster.service.TransactionService
import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList

@Service
class TransactionServiceImpl: TransactionService {

    @Autowired
    lateinit var userRepository: UserRepository

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

    @Autowired
    lateinit var  authenticationRepository: AuthenticationRepository

    override fun processTransaction(transactionInformation: TransactionInformation)    {

        val transactionEntity = TransactionEntity()
        val transactionCurrency = currencyRepository.findByCode(transactionInformation.currencyCode)

        transactionEntity.userId = transactionInformation.requestingUserId
        transactionEntity.transactionTypeId = transactionTypeRepository.findByCode(transactionInformation.transactionType.name).id
        transactionEntity.currencyId = transactionCurrency.id
        transactionEntity.controlSum = transactionInformation.controlSum

        val creditAmount: BigDecimal = transactionInformation.controlSum
        val exchangedAmount = calculateExchange(transactionCurrency.id!!, creditAmount)

        val creditAccount: AccountEntity = accountRepository.findById(transactionInformation.creditAccountId).get()
        transactionEntity.creditAccountId = creditAccount.id
        creditAccount(creditAccount, exchangedAmount)

        if(transactionInformation.transactionType == TransactionType.TRANSFER)  {
            val debitAccount: AccountEntity = accountRepository.findById(transactionInformation.debitAccountId!!).get()
            transactionEntity.debitAccountId = debitAccount.id
            debitAccount(debitAccount, exchangedAmount)
        }

        transactionRepository.save(transactionEntity)

    }

    override fun retrieveUserTransactions(username: String): TransactionReportInformation {
        val userId: Long = authenticationRepository.findByUsername(username).userId!!
        val transactions: List<TransactionEntity> = transactionRepository.findAllByUserId(userId)


        val transactionSummaryList = ArrayList<TransactionSummary>()
        val name: String = userRepository.findById(userId).get().name!!
        transactions.forEach{
            val currencyCode: String = currencyRepository.findById(it.currencyId!!).get().code!!
            val transactionType: String = transactionTypeRepository.findById(it.transactionTypeId!!).get().code!!
            val creditAccountNumber: UUID = accountRepository.findById(it.creditAccountId!!).get().accountNumber!!
            val transactionSummary = TransactionSummary(it.timestamp,
                it.controlSum!!, currencyCode, transactionType, creditAccountNumber)
            transactionSummaryList.add(transactionSummary)
        }

        return TransactionReportInformation(name, transactionSummaryList)
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