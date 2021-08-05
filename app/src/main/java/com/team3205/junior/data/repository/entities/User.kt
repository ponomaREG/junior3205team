package com.team3205.junior.data.repository.entities

/**
 *  Сущность пользователя слоя репозитория
 *  @property id - идентификатор
 *  @property login - название репозитория
 *  @property avatar_url - url аватара пользователя
 */
data class User(
    val id: Int,
    val login: String,
    val avatar_url: String)
