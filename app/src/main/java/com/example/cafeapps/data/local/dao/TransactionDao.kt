package com.example.cafeapps.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.cafeapps.data.local.entity.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions WHERE memberId = :memberId ORDER BY date DESC")
    fun getTransactionsByMember(memberId: Int): Flow<List<Transaction>>

    @Insert
    suspend fun insertTransaction(transaction: Transaction)
}
