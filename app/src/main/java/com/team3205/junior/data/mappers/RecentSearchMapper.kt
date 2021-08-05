package com.team3205.junior.data.mappers

import com.team3205.junior.data.db.entities.RecentSearchDB
import com.team3205.junior.data.repository.entities.RecentSearch

class RecentSearchMapper {
    fun fromRecentSearchDB(recentSearchDB: RecentSearchDB): RecentSearch =
        RecentSearch(recentSearchDB.id, recentSearchDB.search)
}