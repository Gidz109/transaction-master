package za.co.shoprite.moneymarket.transactionmaster.service

import za.co.shoprite.moneymarket.transactionmaster.model.TransactionReportInformation

interface ReportService {
    fun generateTransactionReport(tranactionReportInformation: TransactionReportInformation)
}