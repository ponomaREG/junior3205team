package com.team3205.junior.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.team3205.junior.data.db.dao.RecentSearchDAO
import com.team3205.junior.data.db.dao.RepositoryDAO
import com.team3205.junior.data.db.dao.UserDAO
import com.team3205.junior.data.db.entities.RecentSearchDB
import com.team3205.junior.data.db.entities.RepositoryDB
import com.team3205.junior.data.db.entities.UserDB

@Database(
    entities = [RepositoryDB::class, UserDB::class, RecentSearchDB::class],
    version = 1
)
abstract class AppDb: RoomDatabase() {
    abstract fun getRepositoryDAO(): RepositoryDAO
    abstract fun getUserDAO(): UserDAO
    abstract fun getRecentSearchDAO(): RecentSearchDAO
}