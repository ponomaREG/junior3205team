package com.team3205.junior.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.team3205.junior.data.db.entities.RecentSearchDB

@Dao
abstract class RecentSearchDAO {

    @Query("SELECT * FROM RecentSearchDB ORDER BY id DESC LIMIT :limit;")
    abstract suspend fun getRecentSearches(limit: Int): List<RecentSearchDB>

    @Query("DELETE FROM RecentSearchDB;")
    abstract suspend fun deleteAll()

    @Insert
    abstract suspend fun insert(recentSearchDB: RecentSearchDB): Long
}