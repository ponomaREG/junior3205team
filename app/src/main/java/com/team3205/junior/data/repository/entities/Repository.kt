package com.team3205.junior.data.repository.entities


data class Repository(
    val id: Int,
    val name: String,
    val url: String,
    val api_url: String,
    val language: String?,
    val stargazers_count: Int,
    val owner: User,
    var isSaved: Boolean,
    var isDownloading: Boolean,
    val description: String?
){
    fun getUrlForDownload(): String = "$api_url/zipball"
}
