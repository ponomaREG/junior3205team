package com.team3205.junior.data.mappers

import com.team3205.junior.data.db.entities.UserDB
import com.team3205.junior.data.network.entities.UserResponse
import com.team3205.junior.data.repository.entities.User


/**
 * Mapper для преобразования объекта сущности базы данных и сети пользователя в объект пользователя слоя репозитория
 */
class UserMapper {
    fun fromUserDB(userDB: UserDB): User{
        return User(userDB.id, userDB.login, userDB.avatar_url)
    }

}