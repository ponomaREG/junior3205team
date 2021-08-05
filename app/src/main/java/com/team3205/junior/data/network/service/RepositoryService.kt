package com.team3205.junior.data.network.service

import com.team3205.junior.data.network.entities.RepositoryResponse
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Сервис, предоставляющий доступ к API гитхаба
 */
interface RepositoryService {

    /**
     * Получение репозиториев пользователя
     * @param username - имя пользователя
     * @return - лист репозиториев
     * @see RepositoryResponse
     */
    @GET("/users/{username}/repos")
    suspend fun getRepositoriesByUserName(@Path("username") username: String): List<RepositoryResponse>
}