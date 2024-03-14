package za.co.shoprite.moneymarket.transactionmaster.service.impl

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.springframework.stereotype.Service
import za.co.shoprite.moneymarket.transactionmaster.model.TransactionReportInformation
import za.co.shoprite.moneymarket.transactionmaster.service.ReportService

@Service
class ReportServiceImpl: ReportService {
    override fun generateTransactionReport(tranactionReportInformation: TransactionReportInformation) {
        val document = PDDocument()
        var page = PDPage()
        document.addPage(page)

        var contentStream = PDPageContentStream(document, page)
        contentStream.beginText()
        contentStream.setFont(PDType1Font.HELVETICA, 8f)
        contentStream.newLineAtOffset(90f, 700f)
        contentStream.showText("Transaction Report")
        contentStream.newLineAtOffset(0f, -20f)
        contentStream.showText("Hello ${tranactionReportInformation.name}, please find below a summary of your transactions.")
        contentStream.newLineAtOffset(0f, -20f)
        contentStream.showText("|                   Date                     | Control Sum |  Currency  |  Transaction Type  |               Credit Account Number             |")

        var lineCount = 3
        tranactionReportInformation.transactionSummaryList.forEach {
            if (lineCount == 32) {
                contentStream.endText()
                contentStream.close()
                val page_ = PDPage()
                document.addPage(page_)
                contentStream = PDPageContentStream(document, page_)
                contentStream.beginText()
                contentStream.setFont(PDType1Font.HELVETICA, 8f)
                contentStream.newLineAtOffset(90f, 700f)
                lineCount = 0
            }
            contentStream.newLineAtOffset(0f, -20f)
            contentStream.showText("${it.date} |        ${it.controlSum}        |      ${it.currency}      |      ${it.transactionType}      | ${it.creditAccountNumber}")
            lineCount++
        }
        contentStream.endText()
        contentStream.close()

        document.save("./target/generated-sources/transactionSummary.pdf")
        document.close()
    }
}
