package za.co.shoprite.moneymarket.transactionmaster.service

import za.co.shoprite.moneymarket.transactionmaster.model.TransactionInformation
import za.co.shoprite.moneymarket.transactionmaster.model.TransactionReportInformation

interface TransactionService {
    fun processTransaction(transactionInformation: TransactionInformation)
    fun retrieveUserTransactions(username: String): TransactionReportInformation
}