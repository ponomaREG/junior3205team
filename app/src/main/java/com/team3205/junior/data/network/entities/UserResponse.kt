package com.team3205.junior.data.network.entities

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("login") val login: String,
    @SerializedName("avatar_url") val avatar_url: String
)