package za.co.shoprite.moneymarket.transactionmaster.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import za.co.shoprite.moneymarket.transactionmaster.model.entity.AccountEntity
import java.util.*

@Repository
interface AccountRepository : JpaRepository<AccountEntity, Long> {
    fun findByAccountNumber(accountNumber: UUID): AccountEntity
}