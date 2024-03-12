package za.co.shoprite.moneymarket.transactionmaster.model.entity

import jakarta.persistence.*
import lombok.Getter
import lombok.RequiredArgsConstructor
import lombok.Setter

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "user", schema = "tmsch", catalog = "transactionmaster")
open class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = false, updatable = false)
    open var id: Long? = null

    @Column(name = "name", nullable = false)
    open var name: String? = null

    @Column(name = "surname", nullable = false)
    open var surname: String? = null

    @Column(name = "username", nullable = false)
    open var username: String? = null

}

