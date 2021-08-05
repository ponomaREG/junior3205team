package com.team3205.junior.data.network.entities

import com.google.gson.annotations.SerializedName

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