package za.co.shoprite.moneymarket.transactionmaster.model.entity

import jakarta.persistence.*
import lombok.Getter
import lombok.RequiredArgsConstructor
import lombok.Setter
import java.time.LocalDateTime

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "session", schema = "tmsch", catalog = "transactionmaster")
open class SessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = false, updatable = false)
    open var id: Long? = null

    @Column(name = "timestamp", nullable = false)
    open var timestamp: LocalDateTime? = null

    @Column(name = "authentication_id", nullable = true, insertable = true, updatable = true)
    open var authenticationId: Long? = null

}

