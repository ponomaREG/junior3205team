package com.team3205.junior.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecentSearchDB(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val search: String
)
