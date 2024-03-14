package za.co.shoprite.moneymarket.transactionmaster.model.entity

import jakarta.persistence.*
import lombok.Getter
import lombok.RequiredArgsConstructor
import lombok.Setter
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "transaction", schema = "tmsch", catalog = "transactionmaster")
open class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = false, updatable = false)
    open var id: Long? = null

    @Column(name = "transaction_type_id", nullable = false)
    open var transactionTypeId: Short? = null

    @Column(name = "user_id", nullable = false)
    open var userId: Long? = null

    @Column(name = "debit_account_id", nullable = false)
    open var debitAccountId: Long? = null

    @Column(name = "credit_account_id", nullable = false)
    open var creditAccountId: Long? = null

    @Column(name = "control_sum", nullable = false)
    open var controlSum: BigDecimal? = null

    @Column(name = "currency_id", nullable = false)
    open var currencyId: Short? = null

    @Column(name = "timestamp", nullable = false)
    open var timestamp: LocalDateTime = LocalDateTime.now()

    @Column(name = "session_id", nullable = false)
    open var sessionId: Long? = null

}