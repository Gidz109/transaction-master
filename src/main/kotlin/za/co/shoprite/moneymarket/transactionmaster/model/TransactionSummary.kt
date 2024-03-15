package za.co.shoprite.moneymarket.transactionmaster.model

import za.co.shoprite.moneymarket.transactionmaster.model.enum.TransactionType
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

data class TransactionSummary(val date: LocalDateTime,
                              val controlSum: BigDecimal,
                              val currency: String,
                              val transactionType: String,
                              val creditAccountNumber: UUID
)
