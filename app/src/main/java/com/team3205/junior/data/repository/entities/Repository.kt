package com.team3205.junior.data.repository.entities

/**
 * Сущность репозитория
 * @property id - идентификатор репозитория
 * @property name - название
 * @property url - публичный url(адрес репозитория)
 * @property api_url - api url репозитория
 * @property language - язык разработки
 * @property stargazers_count - количество звезд
 * @property owner - владелец
 * @property isSaved - статус загружен ли репозиторий
 * @property isDownloading - статус скачивается ли в данный момент репозиторий
 * @property description - описание
 */
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

    /**
     *  Получение url для скачивания репозитория
     */
    fun getUrlForDownload(): String = "$api_url/zipball"
}
