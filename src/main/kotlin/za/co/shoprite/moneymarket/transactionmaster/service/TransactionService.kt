package za.co.shoprite.moneymarket.transactionmaster.service

import za.co.shoprite.moneymarket.transactionmaster.model.TransactionInformation

interface TransactionService {
    fun processTransaction(transactionInformation: TransactionInformation)
}