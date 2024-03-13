package za.co.shoprite.moneymarket.transactionmaster.model

import za.co.shoprite.moneymarket.transactionmaster.model.enum.TransactionType
import java.math.BigDecimal

data class TransactionInformation(val transactionType: TransactionType, val requestingUserId: Long,
                                  val debitAccountId: Long?, val creditAccountId: Long, val controlSum: BigDecimal,
                                  val currencyCode: String)
