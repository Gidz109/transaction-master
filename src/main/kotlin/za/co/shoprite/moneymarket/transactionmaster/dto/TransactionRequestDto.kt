package za.co.shoprite.moneymarket.transactionmaster.dto

import lombok.Getter
import lombok.NoArgsConstructor
import lombok.RequiredArgsConstructor
import lombok.Setter
import java.math.BigDecimal
import java.util.*

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
data class TransactionRequestDto(val amount: BigDecimal, val currency: String, val creditAccountNumber: UUID?)