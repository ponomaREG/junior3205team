package com.team3205.junior.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.team3205.junior.data.db.entities.RepositoryDB

/**
 *  Dao для получения информации об сохраненных репозиториев
 */
@Dao
abstract class RepositoryDAO {

    /**
     * Получение сохраненных репозиториев
     * @return лист с репозиториями
     * @see RepositoryDB
     */
    @Query("SELECT * FROM RepositoryDB ORDER BY id DESC")
    abstract suspend fun getSavedRepositories(): List<RepositoryDB>

    /**
     * Получение сохраненных репозиториев конкретного пользователя
     * @param user_id - идентификатор пользователя
     * @return лист с репозиториями
     * @see RepositoryDB
     */
    @Query("SELECT * FROM RepositoryDB where ownerID == :user_id ORDER BY id DESC")
    abstract suspend fun getSavedRepositoriesByOwnerId(user_id: Int): List<RepositoryDB>

    /**
     * Вставка нового репозитория
     * @param repositoryDB - репозитория
     * @return row-id
     */
    @Insert
    abstract suspend fun insert(repositoryDB: RepositoryDB): Long
}