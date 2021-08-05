package com.team3205.junior.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *  Сущность(таблица) пользователя
 *  @property id - идентификатор
 *  @property login - название репозитория
 *  @property avatar_url - url аватара пользователя
 */
@Entity
data class UserDB(
    @PrimaryKey val id: Int,
    val login: String,
    val avatar_url: String
)
