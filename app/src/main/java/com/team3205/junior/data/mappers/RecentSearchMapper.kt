package com.team3205.junior.data.mappers

import com.team3205.junior.data.db.entities.RecentSearchDB
import com.team3205.junior.data.repository.entities.RecentSearch

/**
 * Mapper для преобразования объекта сущности базы данных раннего поиска в объект раннего поиска слоя репозитория
 */
class RecentSearchMapper {
    fun fromRecentSearchDB(recentSearchDB: RecentSearchDB): RecentSearch =
        RecentSearch(recentSearchDB.id, recentSearchDB.search)
}