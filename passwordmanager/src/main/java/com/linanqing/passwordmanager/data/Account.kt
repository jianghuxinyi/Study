package com.linanqing.passwordmanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Account (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val account: String,
    val password: String,
    val email: String,
    val phone: String,
    val remark: String,
        )