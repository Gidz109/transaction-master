package za.co.shoprite.moneymarket.transactionmaster.dto

import lombok.Getter
import lombok.NoArgsConstructor
import lombok.RequiredArgsConstructor
import lombok.Setter
import java.util.UUID

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
data class DepositRequest(val amount: Double, val currency: String) {

    //val controlSum: MyMoney =
}
