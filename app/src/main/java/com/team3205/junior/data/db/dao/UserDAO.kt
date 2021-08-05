package com.team3205.junior.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.team3205.junior.data.db.entities.UserDB

/**
 * Dao для получения пользовательской информации
 */
@Dao
abstract class UserDAO {

    /**
     * Получения объекта пользователя по идентификатору
     * @return объект пользователя
     * @see UserDB
     */
    @Query("SELECT * FROM UserDB where id == :id;")
    abstract suspend fun getSavedUserById(id: Int): UserDB

    /**
     * Получение объекта пользователя по логину
     * @return объект пользователя
     * @see UserDB
     */
    @Query("SELECT * FROM UserDB where login == :username;")
    abstract suspend fun getSavedUserByUserName(username: String): UserDB?

    /**
     * Вставка нового объекта пользователя
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(userDB: UserDB): Long
}