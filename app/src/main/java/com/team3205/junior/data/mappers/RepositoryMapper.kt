package com.team3205.junior.data.mappers

import com.team3205.junior.data.db.entities.RepositoryDB
import com.team3205.junior.data.network.entities.RepositoryResponse
import com.team3205.junior.data.repository.entities.Repository
import com.team3205.junior.data.repository.entities.User

/**
 * Mapper для преобразования объекта сущности базы данных репозитория и сущности сети в объект слоя репозитория
 */
class RepositoryMapper {
    fun fromRepositoryDB(repositoryDB: RepositoryDB, owner: User): Repository{
        return Repository(
            id = repositoryDB.id,
            name = repositoryDB.name,
            url = repositoryDB.url,
            api_url = repositoryDB.api_url,
            language = repositoryDB.language,
            stargazers_count = repositoryDB.stargazers_count,
            owner = owner,
            isSaved = true,
            isDownloading = false,
            description = repositoryDB.description
        )
    }

    fun fromRepositoryResponse(repositoryResponse: RepositoryResponse): Repository{
        return Repository(
            id = repositoryResponse.id,
            name = repositoryResponse.name,
            url = repositoryResponse.url,
            api_url = repositoryResponse.api_url,
            language = repositoryResponse.language,
            stargazers_count = repositoryResponse.stargazers_count,
            owner = User(repositoryResponse.owner.id,repositoryResponse.owner.login, repositoryResponse.owner.avatar_url),
            isSaved = false,
            isDownloading = false,
            description = repositoryResponse.description
        )
    }
}