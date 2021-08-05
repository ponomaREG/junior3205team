package com.team3205.junior.data.mappers

import com.team3205.junior.data.db.entities.RepositoryDB
import com.team3205.junior.data.repository.entities.Repository


/**
 * Mapper для преобразования объекта сущности репозитория в объект репозитория слоя базы данных
 */
class RepositoryDBMapper {
    fun fromRepository(repository: Repository): RepositoryDB {
        return RepositoryDB(
                id = repository.id,
                name = repository.name,
                url = repository.url,
                api_url = repository.api_url,
                language = repository.language,
                description = repository.description,
                stargazers_count = repository.stargazers_count,
                ownerID = repository.owner.id
        )
    }
}