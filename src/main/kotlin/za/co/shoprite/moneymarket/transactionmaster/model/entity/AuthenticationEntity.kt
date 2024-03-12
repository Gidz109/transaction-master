package za.co.shoprite.moneymarket.transactionmaster.model.entity

import jakarta.persistence.*
import lombok.Getter
import lombok.Setter
import lombok.ToString
import org.apache.commons.codec.binary.Base64

@Entity
@Getter
@Setter
@ToString
@Table(name = "authentication", schema = "tmsch", catalog = "transactionmaster")
open class AuthenticationEntity() {

    @PrePersist
    fun encodePassword()    {
        this.password = Base64.encodeBase64String(password.toByteArray())
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = false, updatable = false)
    open var id: Long? = null

    @Column(name = "password", nullable = false)
    open var password: String = ""

    @Column(name = "retry_count", nullable = false)
    open var retryCount: Short = 0

    @Column(name = "locked", nullable = false)
    open var locked: Boolean = false

    @Column(name = "user_id", nullable = false, insertable = false, updatable = false)
    open var userId: Long? = null

}

