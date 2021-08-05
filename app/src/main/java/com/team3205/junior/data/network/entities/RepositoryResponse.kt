package com.team3205.junior.data.network.entities

import com.google.gson.annotations.SerializedName

/**
 *  Серелизованный ответ API гитхаба репозитория
 *  @property id - идентфикатор
 *  @property name - название
 *  @property url -  url репозитория
 *  @property api_url - api url репозитория
 *  @property language - язык разработки
 *  @property stargazers_count - количество звезд
 *  @property owner - владелец
 *  @property description - описание
 */
data class RepositoryResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("html_url") val url: String,
    @SerializedName("url") val api_url: String,
    @SerializedName("language") val language: String?,
    @SerializedName("stargazers_count") val stargazers_count: Int,
    @SerializedName("owner") val owner: UserResponse,
    @SerializedName("description") val description: String?
)