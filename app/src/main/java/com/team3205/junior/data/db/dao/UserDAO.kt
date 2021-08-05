package com.team3205.junior.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.team3205.junior.data.db.entities.UserDB

@Dao
abstract class UserDAO {
    @Query("SELECT * FROM UserDB ORDER BY id DESC;")
    abstract suspend fun getSavedUsers(): List<UserDB>

    @Query("SELECT * FROM UserDB where id == :id;")
    abstract suspend fun getSavedUserById(id: Int): UserDB

    @Query("SELECT * FROM UserDB where login == :username;")
    abstract suspend fun getSavedUserByUserName(username: String): UserDB?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(userDB: UserDB): Long
}