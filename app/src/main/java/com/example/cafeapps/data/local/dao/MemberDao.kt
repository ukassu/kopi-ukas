package com.example.cafeapps.data.local.dao

import androidx.room.*
import com.example.cafeapps.data.local.entity.Member
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {
    @Query("SELECT * FROM members")
    fun getAllMembers(): Flow<List<Member>>

    @Query("SELECT * FROM members WHERE id = :id")
    suspend fun getMemberById(id: Int): Member?

    @Query("SELECT * FROM members WHERE email = :email LIMIT 1")
    suspend fun getMemberByEmail(email: String): Member?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: Member): Long

    @Update
    suspend fun updateMember(member: Member)

    @Delete
    suspend fun deleteMember(member: Member)

    @Query("UPDATE members SET points = points + :points WHERE id = :memberId")
    suspend fun addPoints(memberId: Int, points: Int)

    @Query("UPDATE members SET points = points - :points WHERE id = :memberId")
    suspend fun subtractPoints(memberId: Int, points: Int)
}
