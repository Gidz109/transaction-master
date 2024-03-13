package za.co.shoprite.moneymarket.transactionmaster.model.entity

import jakarta.persistence.*
import lombok.Getter
import lombok.RequiredArgsConstructor
import lombok.Setter
import java.math.BigDecimal

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "exchange_rate", schema = "tmsch", catalog = "transactionmaster")
open class ExchangeRateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = false, updatable = false)
    open var id: Short? = null

    @Column(name = "currency_id", nullable = false)
    open var currencyId: Short? = null

    @Column(name = "exchange_rate", nullable = false)
    open var exchangeRate: BigDecimal? = null

}