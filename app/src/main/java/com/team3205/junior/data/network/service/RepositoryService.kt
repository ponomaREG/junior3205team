package com.team3205.junior.data.network.service

import com.team3205.junior.data.network.entities.RepositoryResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface RepositoryService {
    @GET("/users/{username}/repos")
    suspend fun getRepositoriesByUserName(@Path("username") username: String): List<RepositoryResponse>
}