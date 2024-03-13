package za.co.shoprite.moneymarket.transactionmaster.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import za.co.shoprite.moneymarket.transactionmaster.model.entity.CurrencyEntity

@Repository
interface CurrencyRepository : JpaRepository<CurrencyEntity, Short> {
    fun findByCode(code: String): CurrencyEntity
}