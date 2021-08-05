package com.team3205.junior.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Сущность(таблица) репозитория
 * @property id - идентификатор репозитория
 * @property name - название
 * @property url - публичный url(адрес репозитория)
 * @property api_url - api url репозитория
 * @property language - язык разработки
 * @property stargazers_count - количество звезд
 * @property ownerID - идентификатор владельца
 * @property description - описание
 */
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = UserDB::class,
            parentColumns = ["id"],
            childColumns = ["ownerID"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class RepositoryDB(
    @PrimaryKey val id: Int,
    val name: String,
    val url: String,
    val api_url: String,
    val language: String?,
    val stargazers_count: Int,
    val ownerID: Int,
    val description: String?
)
