package za.co.shoprite.moneymarket.transactionmaster.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import za.co.shoprite.moneymarket.transactionmaster.model.entity.TransactionEntity

@Repository
interface TransactionRepository : JpaRepository<TransactionEntity, Long>    {
    fun findAllByUserId(userId: Long): List<TransactionEntity>
}