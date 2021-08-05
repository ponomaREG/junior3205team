package com.team3205.junior.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.team3205.junior.data.db.entities.RecentSearchDB

/**
 * Dao для получения данных об пршедших запросах пользователя
 */
@Dao
abstract class RecentSearchDAO {

    /**
     * Получение ранних запросов пользователя
     * @param limit - количество результатов
     * @return - лист с ранними запросами
     * @see RecentSearchDB
     */
    @Query("SELECT * FROM RecentSearchDB ORDER BY id DESC LIMIT :limit;")
    abstract suspend fun getRecentSearches(limit: Int): List<RecentSearchDB>

    /**
     * Очистка таблицы
     */
    @Query("DELETE FROM RecentSearchDB;")
    abstract suspend fun deleteAll()

    /**
     * Вставка нового значения, когда пользователь выполняет новый запрос на поиск
     * @see RecentSearchDB
     */
    @Insert
    abstract suspend fun insert(recentSearchDB: RecentSearchDB): Long
}