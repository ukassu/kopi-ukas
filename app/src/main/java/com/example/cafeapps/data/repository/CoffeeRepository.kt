package com.example.cafeapps.data.repository

import com.example.cafeapps.data.local.dao.MemberDao
import com.example.cafeapps.data.local.dao.TransactionDao
import com.example.cafeapps.data.local.entity.Member
import com.example.cafeapps.data.local.entity.Transaction
import kotlinx.coroutines.flow.Flow

class CoffeeRepository(
    private val memberDao: MemberDao,
    private val transactionDao: TransactionDao
) {
    val allMembers: Flow<List<Member>> = memberDao.getAllMembers()

    suspend fun getMemberById(id: Int): Member? = memberDao.getMemberById(id)

    suspend fun getMemberByEmail(email: String): Member? = memberDao.getMemberByEmail(email)

    suspend fun insertMember(member: Member): Long = memberDao.insertMember(member)

    suspend fun addTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction)
        memberDao.addPoints(transaction.memberId, transaction.pointEarned)
    }

    fun getTransactionsByMember(memberId: Int): Flow<List<Transaction>> =
        transactionDao.getTransactionsByMember(memberId)

    suspend fun redeemReward(memberId: Int, points: Int) {
        memberDao.subtractPoints(memberId, points)
    }
}
