package za.co.shoprite.moneymarket.transactionmaster.model.entity

import jakarta.persistence.*
import lombok.Getter
import lombok.RequiredArgsConstructor
import lombok.Setter

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "currency", schema = "tmsch", catalog = "transactionmaster")
open class CurrencyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = false, updatable = false)
    open var id: Short? = null

    @Column(name = "name", nullable = false)
    open var name: String? = null

    @Column(name = "code", nullable = false)
    open var code: String? = null

}