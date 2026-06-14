package com.example.cafeapps.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "members")
data class Member(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String,
    val phone: String,
    val password: String = "password123", // Added password with default for migration
    val points: Int = 0
)
