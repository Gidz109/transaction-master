package za.co.shoprite.moneymarket.transactionmaster.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import za.co.shoprite.moneymarket.transactionmaster.model.entity.ExchangeRateEntity

@Repository
interface ExchangeRateRepository : JpaRepository<ExchangeRateEntity, Short> {
    fun findByCurrencyId(currencyId: Short): ExchangeRateEntity
}