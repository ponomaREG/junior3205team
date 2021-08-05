package com.team3205.junior.data.repository.entities

/**
 * Сущность раннего поиска слоя репозитория
 * @property id - идентификатор репозитория
 * @property search - поиск
 */
data class RecentSearch(
    val id: Int,
    val search: String
)