package com.team3205.junior.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.team3205.junior.data.db.entities.RepositoryDB

@Dao
abstract class RepositoryDAO {

    @Query("SELECT * FROM RepositoryDB ORDER BY id DESC")
    abstract suspend fun getSavedRepositories(): List<RepositoryDB>

    @Query("SELECT * FROM RepositoryDB where ownerID == :user_id ORDER BY id DESC")
    abstract suspend fun getSavedRepositoriesByOwnerId(user_id: Int): List<RepositoryDB>

    @Insert
    abstract suspend fun insert(repositoryDB: RepositoryDB): Long
}