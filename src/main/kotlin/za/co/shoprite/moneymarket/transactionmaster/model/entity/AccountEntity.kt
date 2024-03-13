package za.co.shoprite.moneymarket.transactionmaster.model.entity

import jakarta.persistence.*
import lombok.Getter
import lombok.RequiredArgsConstructor
import lombok.Setter
import java.math.BigDecimal
import java.util.UUID

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "account", schema = "tmsch", catalog = "transactionmaster")
open class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = false, updatable = false)
    open var id: Long? = null

    @Column(name = "balance", nullable = false)
    open var balance: BigDecimal? = null

    @Column(name = "currency_id", nullable = false)
    open var currencyId: Short? = null

    @Column(name = "account_Number", nullable = false)
    open var accountNumber: UUID? = null

    @Column(name = "user_id", nullable = false)
    open var userId: Long? = null

}