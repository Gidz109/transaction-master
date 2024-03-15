package za.co.shoprite.moneymarket.transactionmaster.service

import za.co.shoprite.moneymarket.transactionmaster.dto.TransactionRequestDto
import za.co.shoprite.moneymarket.transactionmaster.model.TransactionInformation
import za.co.shoprite.moneymarket.transactionmaster.model.enum.TransactionType

interface ValidationService {
    fun validateAndBuildTransactionInformation(username: String, transactionType: TransactionType, transactionRequest: TransactionRequestDto): TransactionInformation
}