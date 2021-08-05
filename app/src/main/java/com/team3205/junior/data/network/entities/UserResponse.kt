package com.team3205.junior.data.network.entities

import com.google.gson.annotations.SerializedName

/**
 *  Серелизованный ответ API гитхаба пользователя
 *  @property id - идентификатор пользователя
 *  @property login - название
 *  @property avatar_url - url аватара пользователя
 */
data class UserResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("login") val login: String,
    @SerializedName("avatar_url") val avatar_url: String
)