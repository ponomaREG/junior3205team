package com.team3205.junior.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserDB(
    @PrimaryKey val id: Int,
    val login: String,
    val avatar_url: String
)
