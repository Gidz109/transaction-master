package za.co.shoprite.moneymarket.transactionmaster.model.entity

import jakarta.persistence.*
import lombok.Getter
import lombok.Setter
import lombok.ToString

@Entity
@Getter
@Setter
@ToString
@Table(name = "authentication", schema = "tmsch", catalog = "transactionmaster")
open class AuthenticationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = false, updatable = false)
    open var id: Long? = null

    @Column(name = "username", nullable = false)
    open var username: String = ""

    @Column(name = "retry_count", nullable = false)
    open var retryCount: Short = 0

    @Column(name = "locked", nullable = false)
    open var locked: Boolean = false

    @Column(name = "user_id", nullable = false, insertable = false, updatable = false)
    open var userId: Long? = null

}

